package com.sesxh.smoothhttp.common;

import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author LYH
 * @date 2021/1/14
 * @time 20:34
 * @desc 当需要知道具体哪个请求出错，需要onErrorResumeNext(this)
 **/
public class CommonErrorFunc<T> implements Function<Throwable, Observable<T>> {

    public int flag;
    public CommonErrorFunc(int flag) {
        this.flag=flag;
    }

    @Override
    public Observable<T> apply(Throwable t) {
        SmoothHttpThrowable r;

        if(t instanceof SmoothHttpThrowable) {
            r= (SmoothHttpThrowable) t;
        }else {
            r=ApiException.handleException(t);
        }
        r.flag(flag);
        return Observable.error(r);
    }
}
