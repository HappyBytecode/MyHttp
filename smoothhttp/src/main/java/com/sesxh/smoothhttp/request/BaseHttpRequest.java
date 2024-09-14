package com.sesxh.smoothhttp.request;

import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.sesxh.smoothhttp.HttpGlobalConfig;
import com.sesxh.smoothhttp.api.Api;
import com.sesxh.smoothhttp.transformer.DefaultConverter;
import com.sesxh.smoothhttp.transformer.ResponseStringConverter;
import com.sesxh.smoothhttp.utils.CastUtils;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * @author LYH
 * @date 2021/1/7
 * @time 19:01
 * @desc
 **/
public abstract class BaseHttpRequest<T extends BaseRequest<T>> extends BaseRequest<T>{


    protected BaseHttpRequest(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
        }
        headers.putAll(HttpGlobalConfig.getInstance().getGlobalHeaders());
        params.putAll(HttpGlobalConfig.getInstance().getGlobalParams());
    }

    public Observable<String> request(){
        injectLocalParams();
        return CastUtils.cast(execute(String.class));
    }

    public <K> Observable<K> request(Class<?> type) {
        this.injectLocalParams();
        return CastUtils.cast(execute(type));
    }

    public <K> Observable<K> requestObj(Class<K> type) {
        this.injectLocalParams();
        return CastUtils.cast(execute(type));
    }

    public <K> Observable<List<K>> requestList(Class<K> type) {
        this.injectLocalParams();
        return CastUtils.cast(execute(type,true));
    }

    @Override
    protected void injectLocalParams() {
        super.injectLocalParams();
        mApi = getCommonRetrofit().create(Api.class);
    }

    protected <K> ObservableTransformer<String, K> cast(Class<K> type,boolean isList)  {
        DefaultConverter<K> converter;
        if(isAutoConvert()) {
            try {
                converter = CastUtils.cast(mConvertType.newInstance());
            } catch (Exception e) {
                converter = new DefaultConverter<K>();
            }
        }else {
            converter= CastUtils.cast(new ResponseStringConverter());
        }
        converter.type((type.isArray()||isList)?TypeToken.getParameterized(List.class,type).getType():type);
        final DefaultConverter<K>  c=converter;
        return apiResultObservable -> apiResultObservable
                .map(c);
    }

    protected <K> Observable<K> execute(Class<K> clazz){
        return execute(clazz,false);
    }

    protected abstract <K> Observable<K> execute(Class<K> clazz,boolean isList);
}
