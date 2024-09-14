package com.sesxh.smoothhttp.log;

import android.util.Log;

import com.sesxh.smoothhttp.SmoothHttp;

/**
 * @author LYH
 * @date 2021/1/11
 * @time 9:08
 * @desc 日志工具类
 **/
public class Logger {


    public static void d(String message){
        d(SmoothHttp.TAG,message);
    }

    public static void d(String tag,String message){
        if(SmoothHttp.getInstance().globalConfig().isDebug()){
            Log.d(tag,message);
        }
    }
}
