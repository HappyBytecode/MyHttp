package com.sesxh.smoothhttp.interceptor;


import android.text.TextUtils;

import com.sesxh.smoothhttp.MediaTypes;
import com.sesxh.smoothhttp.log.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 15:03
 * @desc 开发使用，可以模拟接口返回数据。
 */

public class DebugDataInterceptor implements Interceptor {


    private String json;

    private DebugDataInterceptor() {

    }

    public static final DebugDataInterceptor getInstance() {
        return new DebugDataInterceptor();
    }


    public DebugDataInterceptor json(String json) {
        this.json = json;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Logger.d("拦截器数据", json + "");
        Request request = chain.request();
        if (!TextUtils.isEmpty(json)) {
            chain.proceed(request);
            return new Response.Builder()
                    .code(200)
                    .addHeader("Content-Type", "application/json")
                    .body(ResponseBody.create(MediaTypes.APPLICATION_JSON_TYPE, json))
                    .message("这是拦截器模拟数据！！！！")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .build();
        }
        // 未注解不进行拦截
        return chain.proceed(request);
    }
}
