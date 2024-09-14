package com.sesxh.smoothhttp.request;

import android.text.TextUtils;

import com.sesxh.smoothhttp.HttpGlobalConfig;
import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.api.Api;
import com.sesxh.smoothhttp.convert.DefaultConvertFactory;
import com.sesxh.smoothhttp.interceptor.DebugDataInterceptor;
import com.sesxh.smoothhttp.interceptor.log.HttpLoggingInterceptor;
import com.sesxh.smoothhttp.ssl.SmoothX509TrustManager;
import com.sesxh.smoothhttp.ssl.SSLManager;
import com.sesxh.smoothhttp.transformer.DefaultConverter;
import com.sesxh.smoothhttp.transformer.TimeoutDns;
import com.sesxh.smoothhttp.utils.CastUtils;
import com.sesxh.smoothhttp.utils.GsonUtil;

import java.net.Proxy;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:15
 * @desc 所有请求的基类
 **/
public class BaseRequest<T extends BaseRequest<T>> {

    protected  Api mApi;
    protected String url = "";
    protected Map<String, String> headers = new LinkedHashMap<>();//请求头
    protected Map<String, Object> params = new LinkedHashMap<>();//请求参数
    protected final List<Interceptor> interceptors = new ArrayList<>();
    protected final List<Interceptor> netInterceptors = new ArrayList<>();
    protected int readTimeout = getGlobalConfig().getReadTimeout();
    protected int writeTimeout =getGlobalConfig().getWriteTimeout();
    protected int connectTimeout = getGlobalConfig().getConnectTimeout();
    protected int dnsTimeout = getGlobalConfig().getDnsTimeout();
    protected boolean autoConvert=true;
    protected String json;
    protected String baseUrl;
    protected Class<? extends  DefaultConverter> mConvertType=getGlobalConfig().getConvertType();
    protected BehaviorSubject<Integer> mSubject;
    protected SocketFactory mSocketFactory;
    protected Proxy mProxy;


    /**
     * 添加 header map
     *
     * @param headers 请求头 map
     * @return 请求体
     */
    public T addHeaders(@NonNull Map<String, String> headers) {
        this.headers.putAll(headers);
        return CastUtils.cast(this);
    }

    /**
     * 添加单个header
     *
     * @param key   请求头 key
     * @param value 请求头 value
     * @return 请求体
     */
    public T addHeader(@NonNull String key, String value) {
        this.headers.put(key, value);
        return CastUtils.cast(this);
    }

    /**
     * 设置 header map，会覆盖之前的 header
     *
     * @param headers 请求头 map
     * @return 请求体
     */
    public T resetHeader(@NonNull Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
        return CastUtils.cast(this);
    }

    /**
     * 移除指定 header
     *
     * @param key 请求头 key
     * @return 请求体
     */
    public T removeHeader(String key) {
        this.headers.remove(key);
        return CastUtils.cast(this);
    }

    /**
     * 移除全部 header
     *
     * @return 请求体
     */
    public T cleanHeader() {
        this.headers.clear();
        return CastUtils.cast(this);
    }


    /**
     * 添加请求参数map
     *
     * @param params 请求参数 map
     * @return 请求体
     */
    public T addParams(@NonNull Map<String, Object> params) {
        if(params!=null) {
            this.params.putAll(params);
        }
        return CastUtils.cast(this);
    }


    /**
     * 添加单个param
     *
     * @param key   key
     * @param value value
     * @return 请求体
     */
    public T addParam(@NonNull String key, Object value) {
        this.params.put(key, value);
        return CastUtils.cast(this);
    }

    /**
     * 添加请求参数map
     *
     * @param param 请求参数 map
     * @return 请求体
     */
    public <K> T  addParam(@NonNull K param) {

        if(param!=null) {
            String json=GsonUtil.gson().toJson(param);
            addParam(json);
        }
        return CastUtils.cast(this);
    }


    /**
     * 添加请求参数map
     *
     * @param param 请求参数 map
     * @return 请求体
     */
    public T addParam(@NonNull String param) {
        if(!TextUtils.isEmpty(param)) {
            this.params.putAll(GsonUtil.gson().fromJson(param,Map.class));;
        }
        return CastUtils.cast(this);
    }


    /**
     * 设置 param map，会覆盖之前的 param
     *
     * @param params map
     * @return 请求体
     */
    public T resetParam(@NonNull Map<String, Object> params) {
        this.params.clear();
        this.params.putAll(params);
        return CastUtils.cast(this);
    }


    /**
     * 添加请求参数map
     *
     * @param param 请求参数 map
     * @return 请求体
     */
    public <K> T resetParam(@NonNull K param) {

        if(param!=null) {
            String json=GsonUtil.gson().toJson(param);
            resetParam(json);
        }
        return CastUtils.cast(this);
    }


    /**
     * 添加请求参数map
     *
     * @param param 请求参数 map
     * @return 请求体
     */
    public T resetParam(@NonNull String param) {
        if(!TextUtils.isEmpty(param)) {
            this.params.clear();
            this.params.putAll(GsonUtil.gson().fromJson(param,Map.class));;
        }
        return CastUtils.cast(this);
    }


    /**
     * 移除指定 param
     *
     * @param key key
     * @return 请求体
     */
    public T removeParam(String key) {
        this.params.remove(key);
        return CastUtils.cast(this);
    }

    /**
     * 移除全部 params
     *
     * @return 请求体
     */
    public T cleanParams() {
        this.params.clear();
        return CastUtils.cast(this);
    }

    public T baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return CastUtils.cast(this);
    }

    /**
     * 添加 url路径参数
     * @param endUrl 请参数
     * @return
     */
    public T endUrl(String... endUrl) {
        if(endUrl!=null&&endUrl.length>0) {
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<endUrl.length;i++){
                String url=endUrl[i];
                if(!url.endsWith("/")){
                    sb.append(url);
                    if((i<endUrl.length-1)){
                        sb.append("/");
                    }
                }
            }
            if (!url.endsWith("/")) {
                this.url = url + "/";
            }
            this.url=url+sb.toString();
        }
        return CastUtils.cast(this);
    }


    public T interceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return CastUtils.cast(this);
    }

    public T netInterceptor(Interceptor interceptor) {
        netInterceptors.add(interceptor);
        return CastUtils.cast(this);
    }

    public T clearInterceptor() {
        interceptors.clear();
        return CastUtils.cast(this);
    }

    public T clearNetInterceptor() {
        netInterceptors.clear();
        return CastUtils.cast(this);
    }


    public T readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return CastUtils.cast(this);
    }

    public T writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return CastUtils.cast(this);
    }

    public T connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return CastUtils.cast(this);
    }

    public T dnsTimeout(int dnsTimeout) {
        this.dnsTimeout = dnsTimeout;
        return CastUtils.cast(this);
    }

    public T socketFactory(SocketFactory factory) {
        this.mSocketFactory=factory;
        return CastUtils.cast(this);
    }

    public T proxy(Proxy proxy) {
        this.mProxy=proxy;
        return CastUtils.cast(this);
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }

    public T autoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
        return CastUtils.cast(this);
    }

    public String getJson() {
        return json;
    }

    public T json(String json) {
        this.json = json;
        return CastUtils.cast(this);
    }

    public T convert(Class<? extends  DefaultConverter> convertType) {
        this.mConvertType = convertType;
        return CastUtils.cast(this);
    }

    public T life(BehaviorSubject<Integer> subject) {
        this.mSubject=subject;
        return CastUtils.cast(this);
    }

    /**
     * 注入本地配置参数
     */
    protected void injectLocalParams() {
        // 全局配置覆盖本地配置
        getGlobalConfig().resetClientBuilder();
        resetGlobalParams();
        if(mProxy!=null){
            getGlobalConfig().getClientBuilder().proxy(mProxy);
        }
        if(mSocketFactory!=null) {
            getGlobalConfig().getClientBuilder().socketFactory(mSocketFactory);
        }
        getGlobalConfig().getClientBuilder().dns(new TimeoutDns(dnsTimeout,SmoothHttp.getInstance().globalConfig().getTimeUnit()));
        // 添加请求拦截器
        if (!interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                getGlobalConfig().getClientBuilder().addInterceptor(interceptor);
            }
        }
        // 添加请求网络拦截器
        if (!netInterceptors.isEmpty()) {
            for (Interceptor interceptor : netInterceptors) {
                getGlobalConfig().getClientBuilder().addNetworkInterceptor(interceptor);
            }
        }
        //设置局部超时时间和重试次数
        if (readTimeout > 0) {
            getGlobalConfig().getClientBuilder().readTimeout(readTimeout, SmoothHttp.getInstance().globalConfig().getTimeUnit());
        }
        if (writeTimeout > 0) {
            getGlobalConfig().getClientBuilder().writeTimeout(writeTimeout, SmoothHttp.getInstance().globalConfig().getTimeUnit());
        }
        if (connectTimeout > 0) {
            getGlobalConfig().getClientBuilder().connectTimeout(connectTimeout, SmoothHttp.getInstance().globalConfig().getTimeUnit());
        }
    }


    protected Retrofit getCommonRetrofit() {

        Retrofit.Builder retrofitBuilder = getGlobalConfig().getRetrofitBuilder();
        if(retrofitBuilder==null){
            initRetrofitBuilder();
            retrofitBuilder=getGlobalConfig().getRetrofitBuilder();
        }
        retrofitBuilder.baseUrl(SmoothHttp.getInstance().getBaseUrl());
        if(!TextUtils.isEmpty(baseUrl)){
            if(!baseUrl.endsWith("/")){
                baseUrl=baseUrl+"/";
            }
            retrofitBuilder.baseUrl(baseUrl);
        }
        OkHttpClient.Builder okHttpBuilder=getGlobalConfig().getClientBuilder();
        if (getGlobalConfig().isDebug()) {
            // 添加日志拦截器
            // 添加调试阶段的模拟数据拦截器
            okHttpBuilder.addNetworkInterceptor(DebugDataInterceptor.getInstance().json(json));
            okHttpBuilder.addInterceptor(HttpLoggingInterceptor.getInstance());
        }
        OkHttpClient okHttp = getGlobalConfig().getOkHttpClient();

        if(okHttp!=null&&okHttp.dispatcher()!=null){
            okHttpBuilder.dispatcher(okHttp.dispatcher());
            okHttpBuilder.connectionPool(okHttp.connectionPool());
        }
        OkHttpClient newClient = okHttpBuilder.build();
        if(okHttp==null){
            getGlobalConfig().okHttpClient(newClient);
        }
        return retrofitBuilder.client(newClient).build();
    }

    private void initRetrofitBuilder() {
        Retrofit.Builder retrofitBuilder =new Retrofit.Builder();
        String baseUrl=SmoothHttp.getInstance().getBaseUrl();
        if (!TextUtils.isEmpty(baseUrl)) {
            if(!baseUrl.endsWith("/")){
                baseUrl=baseUrl+"/";
                SmoothHttp.getInstance().init(baseUrl);
            }
            retrofitBuilder.baseUrl(SmoothHttp.getInstance().getBaseUrl());
        } else {
            throw new IllegalArgumentException("base url can not be empty !!!");
        }
        if (getGlobalConfig().getConverterFactory() == null) {
            getGlobalConfig().converterFactory(DefaultConvertFactory.create());
        }
        retrofitBuilder.addConverterFactory(getGlobalConfig().getConverterFactory());
        if (getGlobalConfig().getCallAdapterFactories().isEmpty()) {
            getGlobalConfig().addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());//用OkHTTPD的线程池
        }
        for (CallAdapter.Factory factory : getGlobalConfig().getCallAdapterFactories()) {
            retrofitBuilder.addCallAdapterFactory(factory);
        }
        getGlobalConfig().retrofitBuilder(retrofitBuilder);
    }

    protected HttpGlobalConfig getGlobalConfig() {
        return SmoothHttp.getInstance().globalConfig();
    }





    /**
     * 使用全局配置覆盖当前配置
     */
    private void resetGlobalParams() {
       
        if (getGlobalConfig().getHostnameVerifier() == null) {
            getGlobalConfig().hostVerifier(new SSLManager.SafeHostnameVerifier(SmoothHttp.getInstance().getBaseUrl()));
        }
        getGlobalConfig().getClientBuilder().hostnameVerifier(getGlobalConfig().getHostnameVerifier());

        if (getGlobalConfig().getSslSocketFactory() == null) {
            getGlobalConfig().sslFactory(SSLManager.getSslSocketFactory(null, null, null));
        }
        SSLContext sslContext = null;
        final X509TrustManager x509TrustManager = new SmoothX509TrustManager();
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new X509TrustManager[]{x509TrustManager}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(getGlobalConfig().getProxy()!=null){
            getGlobalConfig().getClientBuilder().proxy(getGlobalConfig().getProxy());
        }
        getGlobalConfig().getClientBuilder().socketFactory(getGlobalConfig().getSocketFactory());
        getGlobalConfig().getClientBuilder().sslSocketFactory(getGlobalConfig().getSslSocketFactory(),x509TrustManager);
        getGlobalConfig().getClientBuilder().connectTimeout(getGlobalConfig().getConnectTimeout(), SmoothHttp.getInstance().globalConfig().getTimeUnit());
        getGlobalConfig().getClientBuilder().readTimeout(getGlobalConfig().getReadTimeout(), SmoothHttp.getInstance().globalConfig().getTimeUnit());
        getGlobalConfig().getClientBuilder().writeTimeout(getGlobalConfig().getWriteTimeout(), SmoothHttp.getInstance().globalConfig().getTimeUnit());
        // 添加全局的拦截器
        for (Interceptor interceptor : getGlobalConfig().getInterceptors()) {
            getGlobalConfig().getClientBuilder().addInterceptor(interceptor);
        }
        for (Interceptor interceptor : getGlobalConfig().getNetInterceptors()) {
            getGlobalConfig().getClientBuilder().addNetworkInterceptor(interceptor);
        }
        getGlobalConfig().getClientBuilder().retryOnConnectionFailure(false);
    }

}
