package com.sesxh.smoothhttp.transformer;

import com.sesxh.smoothhttp.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 15:31
 * @desc 超时重试
 */

public class RetryFunc implements Function<Observable<Throwable>, ObservableSource<?>> {

    private int count = 1;
    private final int maxCount;
    private final long retryDelayMillis;

    public RetryFunc(int maxCount, long retryDelayMillis) {
        this.maxCount = maxCount;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwable) {
        return throwable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable1) throws Exception {
                if (++count <= maxCount && (throwable1 instanceof SocketTimeoutException
                        || throwable1 instanceof ConnectException)) {
                    return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
                }
                throw ApiException.handleException(throwable1);
            }
        });
    }
}
