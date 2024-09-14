package com.sesxh.smoothhttp.request.io;

import android.text.TextUtils;

import com.sesxh.smoothhttp.HttpGlobalConfig;
import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.api.Api;
import com.sesxh.smoothhttp.callbacks.TransmitCallback;
import com.sesxh.smoothhttp.request.BaseHttpRequest;
import com.sesxh.smoothhttp.request.BaseRequest;
import com.sesxh.smoothhttp.transformer.DefaultConverter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.ConnectionPool;

import static com.sesxh.smoothhttp.interceptor.log.HttpLoggingInterceptor.IO_FLAG_HEADER;

/**
 * @author LYH
 * @date 2021/1/12
 * @time 11:34
 * @desc 封装上传和下载
 */


public abstract class IORequest<T extends BaseRequest<T>> extends BaseRequest<T> {

    private static final int DEFAULT_CONNECT_TIME_OUT= 10;
    private static final int DEFAULT_READ_TIME_OUT= 10*60;
    private static final int DEFAULT_WHITE_TIME_OUT= 10*60;

    protected IORequest(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
        }
        readTimeout(DEFAULT_READ_TIME_OUT);
        writeTimeout(DEFAULT_WHITE_TIME_OUT);
        connectTimeout(DEFAULT_CONNECT_TIME_OUT);
    }


    @Override
    protected void injectLocalParams() {
        super.injectLocalParams();
        mApi = getCommonRetrofit().create(Api.class);
    }


    protected abstract <T extends TransmitCallback> void execute(T callback);


}
