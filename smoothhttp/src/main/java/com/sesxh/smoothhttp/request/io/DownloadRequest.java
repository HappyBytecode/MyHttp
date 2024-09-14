package com.sesxh.smoothhttp.request.io;

import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.callbacks.DownloadCallBack;
import com.sesxh.smoothhttp.callbacks.TransmitCallback;
import com.sesxh.smoothhttp.interceptor.DownloadInterceptor;
import com.sesxh.smoothhttp.lifecycle.LifeCycle;
import com.sesxh.smoothhttp.log.Logger;
import com.sesxh.smoothhttp.transformer.RxScheduler;
import com.sesxh.smoothhttp.utils.FileUtils;


/**
 * @author LYH
 * @date 2021/1/12
 * @time 11:39
 * @desc 下载, 注意运行时权限
 */

public class DownloadRequest extends IORequest<DownloadRequest> {

    private static String TAG = SmoothHttp.TAG + "———Download";
    private String path = FileUtils.getDownloadPath();
    private String name;


    public DownloadRequest(String url) {
        super(url);
    }


    public DownloadRequest target(String path, String fileName) {
        this.path = path;
        this.name = fileName;
        return this;
    }

    public DownloadRequest target(String fileName) {
        this.name = fileName;
        return this;
    }


    @SuppressWarnings("checkResult")
    @Override
    protected <T extends TransmitCallback> void execute(T t) {
        DownloadCallBack callback;
        if (t instanceof DownloadCallBack) {
            callback = (DownloadCallBack) t;
            callback.target(path, name);
        } else {
            return;
        }
        mApi.downFile(url, headers, params)
                .doOnSubscribe(disposable -> {
                    FileUtils.createOrExistsDir(path);
                })
                .compose(LifeCycle.create().initSubject(mSubject).bindLife())
                .compose(RxScheduler.io())
                .subscribe(callback);
    }

    public void request(DownloadCallBack callback) {
        netInterceptor(DownloadInterceptor.getInstance().callback(callback));
        injectLocalParams();
        execute(callback);
    }
}
