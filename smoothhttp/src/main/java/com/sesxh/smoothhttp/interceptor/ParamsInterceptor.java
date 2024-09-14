package com.sesxh.smoothhttp.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:20
 * @desc 添加公共请求参数
 */


public class ParamsInterceptor implements Interceptor {
    private static final String POST = "POST";
    private static final String GET = "GET";
    private Map<String, Object> paramsMap;

    private ParamsInterceptor() {

    }

    public static final ParamsInterceptor getInstance() {
        return new ParamsInterceptor();
    }


    public ParamsInterceptor setParams(Map<String, Object> params) {
        this.paramsMap = params;
        return this;
    }


    /**
     * only support post and get request
     */
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        if (paramsMap.size() <= 0) {
            return chain.proceed(request);
        }
        if (request.method().equals(POST)) {
            if (request.body() instanceof FormBody) {  // 表单
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
                for (Object o : paramsMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    newFormBodyBuilder.add((String) entry.getKey(), value.toString());
                }
                FormBody oldFormBody = (FormBody) request.body();
                int paramSize = oldFormBody.size();
                for (int i = 0; i < paramSize; i++) {
                    newFormBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
                }
                requestBuilder.post(newFormBodyBuilder.build());
            } else if (request.body() instanceof MultipartBody) {  //MultipartBody
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                for (Object o : paramsMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    multipartBuilder.addFormDataPart((String) entry.getKey(), value.toString());
                }

                List<MultipartBody.Part> oldParts = ((MultipartBody) request.body()).parts();
                for (MultipartBody.Part part : oldParts) {
                    multipartBuilder.addPart(part);
                }
                requestBuilder.post(multipartBuilder.build());
            } else if (request.body() != null && request.body().contentLength() == 0L) {
                //无参时提交公共参数
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
                for (Object o : paramsMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    Object value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    newFormBodyBuilder.add((String) entry.getKey(), value.toString());
                }
                requestBuilder.post(newFormBodyBuilder.build());
            }
        } else if (request.method().equals(GET)) {
            HttpUrl.Builder builder = request.url().newBuilder();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }

                builder.setQueryParameter(entry.getKey(), value.toString());
            }
            requestBuilder.url(builder.build());
        }
        request = requestBuilder.build();
        return chain.proceed(request);
    }
}
