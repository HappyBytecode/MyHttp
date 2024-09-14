package com.sesxh.smoothhttp.interceptor;

import com.sesxh.smoothhttp.bodys.ProgressUploadBody;
import com.sesxh.smoothhttp.callbacks.TransmitCallback;
import java.io.IOException;
import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:51
 * @desc 上传拦截器
 */

public class UploadInterceptor implements Interceptor {

    private TransmitCallback mCallback;

    public UploadInterceptor(@NonNull TransmitCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        ProgressUploadBody requestBody = new ProgressUploadBody(request.body(), mCallback);
        Request finalRequest = request.newBuilder()
                .addHeader("Connection", "alive")
                .method(request.method(), requestBody)
                .build();
        return chain.proceed(finalRequest);
    }
}
