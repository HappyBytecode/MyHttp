package com.sesxh.myapplication;

import android.app.Application;

import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.entity.NullDataValue;

/**
 * @author LYH
 * @date 2021/1/11
 * @time 16:30
 * @desc
 **/
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SmoothHttp.getInstance().init("http://apis.juhe.cn/").globalConfig().addGlobalHeader("1","2")
                .addGlobalParam("2","3").defaultValue(new NullDataValue().defString("05")).hosts().debug(true);
    }
}
