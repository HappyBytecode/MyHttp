package com.sesxh.smoothhttp.request;

import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.lifecycle.LifeCycle;
import com.sesxh.smoothhttp.log.Logger;
import com.sesxh.smoothhttp.transformer.RxScheduler;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @author LYH
 * @date 2021/1/7
 * @time 14:08
 * @desc get请求
 */



public class GetRequest extends BaseHttpRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public <K> Observable<K> execute(Class<K> clazz,boolean isList) {
        return mApi.get(url, headers, params)
                .compose(LifeCycle.create().initSubject(mSubject).bindLife())
                .compose(cast(clazz,isList))
                .compose(RxScheduler.retrySync());
    }

}
