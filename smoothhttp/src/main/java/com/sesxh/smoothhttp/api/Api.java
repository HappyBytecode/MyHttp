package com.sesxh.smoothhttp.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * @author LYH
 * @date 2021/1/7
 * @time 9:36
 * @desc 网络请求类型
 */


public interface Api {

    @GET()
    Observable<String> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> maps);

    @POST()
    Observable<String> post(@Url() String url, @HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST()
    Observable<String> postQuery(@Url() String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> maps);

    @FormUrlEncoded
    @POST()
    Observable<String> postForm(@Url() String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, Object> maps);


    @Streaming
    @GET()
    Observable<ResponseBody> downFile(@Url() String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> maps);

    @Streaming
    @POST()
    Observable<ResponseBody> downFile(@Url() String url, @HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(@Url() String url, @Part() List<MultipartBody.Part> parts);


}
