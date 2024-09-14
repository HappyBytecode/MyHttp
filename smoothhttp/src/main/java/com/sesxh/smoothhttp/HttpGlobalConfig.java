package com.sesxh.smoothhttp;

import com.sesxh.smoothhttp.entity.NullDataValue;
import com.sesxh.smoothhttp.ssl.SSLManager;
import com.sesxh.smoothhttp.transformer.DefaultConverter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.annotations.NonNull;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * @author LYH
 * @date 2021/1/5
 * @time 14:24
 * @desc 网络请求配置项
 **/
public class HttpGlobalConfig {

    public static final int DEFAULT_MAX_IDLE_CONNECTIONS = 5;//默认空闲连接数
    public static final long DEFAULT_KEEP_ALIVE_DURATION = 1;//默认心跳间隔时长（分钟）
    private static final int DEFAULT_TIMEOUT = 10;//默认超时时间（秒）
    private static HttpGlobalConfig sHttpGlobalConfig;
    private final List<Interceptor> interceptors = new ArrayList<>();
    private final List<Interceptor> netInterceptors = new ArrayList<>();
    private final List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();//Call适配器工厂
    private Converter.Factory converterFactory;//转换工厂,默认为  GsonConverterFactory
    private final Map<String, String> globalHeaders = new LinkedHashMap<>();//全局请求头
    private final Map<String, Object> globalParams = new LinkedHashMap<>();//全局请求参数
    private SSLSocketFactory sslSocketFactory;//SSL工厂
    private HostnameVerifier hostnameVerifier;//主机域名验证
    private boolean isDebug = true;    // 是否是debug环境
    private boolean trustAll = true;   // 不验证 host 信任所有的 host
    private Proxy mProxy;//代理对象
    private long retryDelayMillis;//请求失败重试间隔时间
    private int connectTimeout = DEFAULT_TIMEOUT; //连接超时时间，默认 10 秒
    private int readTimeout = DEFAULT_TIMEOUT; //读取超时时间，默认 10 秒
    private int writeTimeout = DEFAULT_TIMEOUT; //写入超时时间，默认 10 秒
    private int dnsTimeout = DEFAULT_TIMEOUT; //连接超时时间，默认
    private int retryCount;//请求失败重试次数
    private TimeUnit mTimeUnit = TimeUnit.SECONDS;//超时时间单位,默认秒
    private NullDataValue defValues = null;    // Gson 解析补空默认值
    private OkHttpClient.Builder mOkHttpBuilder=getDefaultClientBuilder();//TODO :目前每次请求都会新建一个OkHttpClient。
    private OkHttpClient mOkHttpClient;//TODO :目前每次请求都会新建一个OkHttpClient。
    private Retrofit.Builder mRetrofitBuilder;
    private SocketFactory mSocketFactory;
    private Class<? extends  DefaultConverter> convertType=DefaultConverter.class;


    public static HttpGlobalConfig getInstance() {
        if (sHttpGlobalConfig == null) {
            synchronized (HttpGlobalConfig.class) {
                if (sHttpGlobalConfig == null) {
                    sHttpGlobalConfig = new HttpGlobalConfig();
                }
            }
        }
        return sHttpGlobalConfig;
    }

    private HttpGlobalConfig() {
        addGlobalHeader("X-Requested-With", "XMLHttpRequest");
        if (getHostnameVerifier() == null) {
            hostVerifier(new SSLManager.SafeHostnameVerifier(SmoothHttp.getInstance().getBaseUrl()));
        }
        if (getSslSocketFactory() == null) {
            sslFactory(SSLManager.getSslSocketFactory(null, null, null));
        }
    }


    /**
     * @param factory the factory to add
     * @return Config self
     */
    public HttpGlobalConfig sslFactory(@NonNull SSLSocketFactory factory) {
        this.sslSocketFactory = factory;
        return this;
    }

    /**
     * @param factory the factory to add
     * @return Config self
     */
    public HttpGlobalConfig socketFactory(@NonNull SocketFactory factory) {
        this.mSocketFactory = factory;
        return this;
    }

    /**
     * add a HostnameVerifier,the Request will add your baseUrl as default,if you want add other host
     * call this method
     *
     * @param verifier the hostname verifier
     * @return Config self
     */
    public HttpGlobalConfig hostVerifier(@NonNull HostnameVerifier verifier) {
        this.hostnameVerifier = verifier;
        return this;
    }

    /**
     * 配置 Gson 解析的默认值
     *
     * @param defValues 默认值对象
     * @return 默认值
     */
    public HttpGlobalConfig defaultValue(@NonNull NullDataValue defValues) {
        this.defValues = defValues;
        return this;
    }


    /**
     * 添加安全认证的 hosts
     *
     * @param hosts hosts 地址
     * @return config self
     */
    public HttpGlobalConfig hosts(String... hosts) {
        // 默认添加基础域名
        if (this.hostnameVerifier == null) {
            this.hostnameVerifier = new SSLManager.SafeHostnameVerifier(hosts);
            ((SSLManager.SafeHostnameVerifier) this.hostnameVerifier).addHost(SmoothHttp.getInstance().getBaseUrl());
        } else {
            if (this.hostnameVerifier instanceof SSLManager.SafeHostnameVerifier) {
                ((SSLManager.SafeHostnameVerifier) this.hostnameVerifier).addHosts(Arrays.asList(hosts));
                ((SSLManager.SafeHostnameVerifier) this.hostnameVerifier).addHost(SmoothHttp.getInstance().getBaseUrl());
            } else {
                throw new IllegalArgumentException("please verifier host in your custom hostnameVerifier");
            }
        }
        return this;
    }


    /**
     * add globalHeader this header will be added with every request
     *
     * @param key    header name
     * @param header header value
     * @return Config self
     */
    public HttpGlobalConfig addGlobalHeader(@NonNull String key, String header) {
        this.globalHeaders.put(key, header);
        return this;
    }

    /**
     * add globalHeader by map
     *
     * @param headers http request headers
     * @return Config self
     */
    public HttpGlobalConfig globalHeader(@NonNull Map<String, String> headers) {
        this.globalHeaders.clear();
        this.globalHeaders.putAll(headers);
        return this;
    }

    /**
     * add localParams,the params will be added with every HttpRequest exclude RetrofitRequest
     *
     * @param params the params
     * @return config self
     */
    public HttpGlobalConfig globalParams(@NonNull Map<String, String> params) {
        this.globalParams.clear();
        this.globalParams.putAll(params);
        return this;
    }

    /**
     * add globalParam,the param will be added with every HttpRequest exclude RetrofitRequest
     *
     * @param key   the paramsKey
     * @param param the paramValue
     * @return config self
     */
    public HttpGlobalConfig addGlobalParam(@NonNull String key, String param) {
        this.globalParams.put(key, param);
        return this;
    }

    public HttpGlobalConfig okHttpClient(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
        return this;
    }

    public HttpGlobalConfig retrofitBuilder(Retrofit.Builder retrofitBuilder) {
        mRetrofitBuilder = retrofitBuilder;
        return this;
    }


    /**
     * if you open retry strategy,you can set delay between tow requests
     *
     * @param retryDelay delay time (unit is 'ms')
     * @return config self
     */
    public HttpGlobalConfig retryDelayMillis(long retryDelay) {
        this.retryDelayMillis = retryDelay;
        return this;
    }

    /**
     * set retry count
     *
     * @param retryCount retryCount
     * @return config self
     */
    public HttpGlobalConfig retryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public HttpGlobalConfig addInterceptor(@NonNull Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public HttpGlobalConfig addNetInterceptor(@NonNull Interceptor interceptor) {
        netInterceptors.add(interceptor);
        return this;
    }

    public HttpGlobalConfig addCallAdapterFactory(@NonNull CallAdapter.Factory factory) {
        this.callAdapterFactories.add(factory);
        return this;
    }

    public HttpGlobalConfig converterFactory(@NonNull Converter.Factory factory) {
        this.converterFactory = factory;
        return this;
    }

    public HttpGlobalConfig readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpGlobalConfig writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }
    //连接超时时间
    public HttpGlobalConfig connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HttpGlobalConfig dnsTimeout(int dnsTimeout) {
        this.dnsTimeout = dnsTimeout;
        return this;
    }

    public HttpGlobalConfig timeUnit(TimeUnit timeUnit) {
        mTimeUnit = timeUnit;
        return this;
    }


    /**
     * 配置是否允许所有 host
     *
     * @param trustAll 是否允许所有host
     * @return config
     */
    public HttpGlobalConfig trustAllHost(boolean trustAll) {
        this.trustAll = trustAll;
        return this;
    }

    public HttpGlobalConfig resetClientBuilder() {
        mOkHttpBuilder=getDefaultClientBuilder();
        return this;
    }

    public HttpGlobalConfig convert(Class<? extends  DefaultConverter> convertType) {
        this.convertType=convertType;
        return this;
    }

    public HttpGlobalConfig setProxy(Proxy proxy) {
        if(proxy==null){
            return this;
        }
        mProxy = proxy;
        return this;
    }

    //    ######################################## getter ########################################


    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public List<Interceptor> getNetInterceptors() {
        return netInterceptors;
    }

    public List<CallAdapter.Factory> getCallAdapterFactories() {
        return callAdapterFactories;
    }

    public Converter.Factory getConverterFactory() {
        return converterFactory;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public SocketFactory getSocketFactory() {
        return mSocketFactory==null?SocketFactory.getDefault():mSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }


    public Map<String, String> getGlobalHeaders() {
        return globalHeaders;
    }

    public Map<String, Object> getGlobalParams() {
        return globalParams;
    }

    public long getRetryDelayMillis() {
        return retryDelayMillis;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public HttpGlobalConfig debug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public boolean isTrustAll() {
        return trustAll;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getDnsTimeout() {
        return dnsTimeout;
    }

    public TimeUnit getTimeUnit() {
        return mTimeUnit;
    }


    public NullDataValue getDefValues() {
        return defValues;
    }

    public OkHttpClient.Builder getClientBuilder() {
        return mOkHttpBuilder;
    }

    public Class<? extends  DefaultConverter> getConvertType() {
        return convertType;
    }


    public Proxy getProxy() {
        return mProxy;
    }

    private OkHttpClient.Builder getDefaultClientBuilder() {
        // 默认加密套件
        ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2).build();
        List<ConnectionSpec> specs = new ArrayList<>();
        specs.add(cs);
        specs.add(ConnectionSpec.COMPATIBLE_TLS);
        specs.add(ConnectionSpec.CLEARTEXT);
        return new OkHttpClient.Builder().connectionSpecs(specs);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return mRetrofitBuilder;
    }

}
