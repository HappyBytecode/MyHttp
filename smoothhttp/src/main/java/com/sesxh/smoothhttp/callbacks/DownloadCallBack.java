package com.sesxh.smoothhttp.callbacks;

import androidx.annotation.NonNull;
import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;
import com.sesxh.smoothhttp.utils.FileUtils;
import com.sesxh.smoothhttp.utils.ThreadUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;


/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:30
 * @desc 下载回调
 */


public abstract class DownloadCallBack extends DisposableObserver<ResponseBody> implements TransmitCallback {

    private String path;
    private String name;
    private File targetFile;


    public void target(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public void target(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void onNext(@NonNull ResponseBody body) {

        InputStream is=null;
        FileOutputStream fos = null;
        Exception resultE =null;
        try {
            is = body.byteStream();
            FileUtils.createOrExistsFile(path + name);
            targetFile = new File(path + name);
            fos = new FileOutputStream(targetFile);
            int len;
            byte[] buffer = new byte[2048];
            while (-1 != (len = is.read(buffer))) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            resultE=e;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                is.close();
            } catch (Exception e) {
                resultE=e;
            }
        }
        if(resultE==null) {
            ThreadUtils.getMainHandler().post(() -> onFinish(true));
        }else {
            onError(ApiException.handleException(resultE));
        }

    }

    @Override
    public void onError(@NonNull Throwable e) {
        ThreadUtils.getMainHandler().post(() -> {
            if (e instanceof SmoothHttpThrowable) {
                onFailure((SmoothHttpThrowable) e);
            } else {
                onFailure(SmoothHttpThrowable.unknown(e));
            }
            onFinish(false);
        });
    }


    @Override
    public void onFinish(boolean success) {
        onFinish(success,targetFile);
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onFailure(SmoothHttpThrowable t) {

    }

    public abstract void onFinish(boolean success,File file);

}
