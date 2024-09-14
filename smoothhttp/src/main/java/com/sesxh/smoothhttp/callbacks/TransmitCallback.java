package com.sesxh.smoothhttp.callbacks;


import com.sesxh.smoothhttp.SmoothHttpThrowable;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:29
 * @desc  上传和下载请求回调接口
 */


public interface TransmitCallback {

    /**
     * 上传/下载完成
     */
    void onFinish(boolean success);


    /**
     * 上传/下载失败
     *
     * @param t 异常类
     */
    void onFailure(SmoothHttpThrowable t);

    /**
     * 上传/下载进度
     *
     * @param progress 进度 % 单位
     */
    void onProgressUpdate(int progress);
}
