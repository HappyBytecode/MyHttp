package com.sesxh.smoothhttp.observer;


import androidx.annotation.NonNull;

import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;

import io.reactivex.observers.DisposableObserver;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:47
 * @desc 一个简单的Observer
 */


public abstract class SmoothHttpObserver<T> extends DisposableObserver<T> {

    private boolean mIsSuccess;

    @Override
    public void onNext(@NonNull T t) {
        try {
            onSuccess(t);
            mIsSuccess=true;
        } catch (Exception e) {
            onError(e);
        }

    }

    @Override
    public void onError(@NonNull Throwable t) {
        mIsSuccess=false;
        SmoothHttpThrowable throwable;
        if(t instanceof SmoothHttpThrowable){
            throwable= (SmoothHttpThrowable) t;
        }else {
            throwable=ApiException.handleException(t);
        }
        onFailure(throwable);
        onFinish(false);
    }


    @Override
    public void onComplete() {
        onFinish(mIsSuccess);
    }

    /**
     * 成功回调
     *
     * @param data 请求成功后的数据体
     */
    protected abstract void onSuccess(T data);

    /**
     * 请求失败回调
     *
     * @param t 失败异常对象
     */
    protected  void onFailure(SmoothHttpThrowable t){

    }

    /**
     * 结束回调
     *
     * @param success 是否成功获取到数据
     */
    protected void onFinish(boolean success){

    }
}
