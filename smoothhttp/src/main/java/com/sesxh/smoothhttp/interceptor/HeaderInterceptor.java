package com.sesxh.smoothhttp.interceptor;


import android.util.Log;

import java.io.IOException;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 14:47
 * @desc 请求头拦截器，为每一次请求添加请求头
 */

public class HeaderInterceptor implements Interceptor {

    /**
     * 设置的 header
     */
    private Map<String, String> headers;

    private HeaderInterceptor() {

    }

    public static final HeaderInterceptor getInstance() {
        return new HeaderInterceptor();
    }


    public HeaderInterceptor setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Log.e("12345",headers.toString());
        Request.Builder builder = chain.request().newBuilder();
        if (checkNotNull(headers)) {
            for (Map.Entry<String, String> item : headers.entrySet()) {
                builder.header(item.getKey(), item.getValue());
            }
        }
        return chain.proceed(builder.build());
    }

    private boolean checkNotNull(Map map) {
        return map != null && !map.isEmpty();
    }
}
