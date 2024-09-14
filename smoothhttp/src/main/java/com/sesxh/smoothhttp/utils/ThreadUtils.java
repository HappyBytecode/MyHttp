package com.sesxh.smoothhttp.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @author LYH
 * @date 2021/1/5
 * @time 18:31
 * @desc 获取主线程Handler
 */


public class ThreadUtils {

    private static Handler mainHandler;
    public static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }
}
