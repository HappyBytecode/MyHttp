package com.sesxh.smoothhttp.interceptor;


import android.text.TextUtils;

import com.sesxh.smoothhttp.bodys.ProgressDownloadBody;
import com.sesxh.smoothhttp.callbacks.DownloadCallBack;
import com.sesxh.smoothhttp.callbacks.TransmitCallback;

import java.io.IOException;
import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:50
 * @desc 下载拦截器
 */


public class DownloadInterceptor implements Interceptor {

    private TransmitCallback mCallback;

    private DownloadInterceptor() {

    }

    public static final DownloadInterceptor getInstance() {
        return new DownloadInterceptor();
    }


    public DownloadInterceptor callback(TransmitCallback callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request=chain.request();
        Response response = chain.proceed(request);
        if(mCallback instanceof DownloadCallBack){
           if(TextUtils.isEmpty( ((DownloadCallBack) mCallback).getName())){
               ((DownloadCallBack) mCallback).target(getFileName(request.url().toString()));
           }
        }
        ProgressDownloadBody responseBody = new ProgressDownloadBody(response.body(), mCallback);
        return response.newBuilder()
                .addHeader("Accept-Encoding", "identity")
                .body(responseBody)
                .build();
    }

    private String getFileName(String source) {
        int end=source.indexOf("?");
        if(end==-1){
            end=source.length();
        }
        String fileName=source.substring(source.lastIndexOf("/")+1,end);
        return fileName;
    }
}
