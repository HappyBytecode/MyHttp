package com.sesxh.smoothhttp.callbacks;



import androidx.annotation.NonNull;

import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;
import com.sesxh.smoothhttp.utils.ThreadUtils;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;


/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:50
 * @desc 上传回调
 */


public abstract class UploadCallBack extends DisposableObserver<ResponseBody> implements TransmitCallback {


    @Override
    public void onError(@NonNull Throwable e) {
        ThreadUtils.getMainHandler().post(() -> {
            if (e instanceof SmoothHttpThrowable) {
                onFailure((SmoothHttpThrowable) e);
            } else {
                onFailure(ApiException.handleException(e));
            }
            onFinish(false);
        });
    }

    @Override
    public void onNext(@NonNull ResponseBody body) {

    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    @Override
    public void onFailure(SmoothHttpThrowable t) {
    }

    @Override
    public void onComplete() {
        ThreadUtils.getMainHandler().postDelayed(() -> onFinish(true)
                , 500);
    }
}
