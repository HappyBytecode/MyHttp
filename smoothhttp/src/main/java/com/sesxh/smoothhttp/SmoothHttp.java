package com.sesxh.smoothhttp;

import android.content.Context;

import com.sesxh.smoothhttp.request.GetRequest;
import com.sesxh.smoothhttp.request.io.DownloadRequest;
import com.sesxh.smoothhttp.request.io.UploadRequest;
import com.sesxh.smoothhttp.request.post.PostRequest;

/**
 * @author LYH
 * @date 2021/1/7
 * @time 9:40
 * @desc 网络请求框架
 **/
public class SmoothHttp {

    public static final String TAG = "SmoothHttp";
    private String mBaseUrl;


    private SmoothHttp() {

    }

    private static class SingletonInstance {
        private static final SmoothHttp instance = new SmoothHttp();
    }

    public static synchronized SmoothHttp getInstance() {
        return SingletonInstance.instance;
    }


    public SmoothHttp init(String baseUrl) {
        this.mBaseUrl = baseUrl;
        return this;
    }


    public String getBaseUrl() {
        return mBaseUrl;
    }

    //获取默认请求配置
    public HttpGlobalConfig globalConfig() {
        return HttpGlobalConfig.getInstance();
    }


    /**
     * normal get request
     */
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    /**
     * normal post request
     */
    public static PostRequest post(String url) {
        return new PostRequest(url);
    }


    /**
     * download request
     *
     * @param url download url
     */
    public static DownloadRequest download(String url) {
        return new DownloadRequest(url);
    }

    /**
     * 上传文件
     *
     * @param url 上传地址
     */
    public static UploadRequest upload(String url) {
        return new UploadRequest(url);
    }







}
