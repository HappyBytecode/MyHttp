package com.sesxh.smoothhttp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesxh.smoothhttp.gsonadapters.DefaultTypeAdapters;

import java.math.BigDecimal;

/**
 * @author LYH
 * @date 2021/1/12
 * @time 9:47
 * @desc
 */



public class GsonUtil {

    private static Gson gson;

    public static synchronized Gson gson() {
        if (gson == null) {
            gson = create();
        }
        return gson;
    }

    private static Gson create() {
        return new GsonBuilder()
                .registerTypeAdapter(Boolean.class, DefaultTypeAdapters.BOOLEAN)
                .registerTypeAdapter(Boolean.class, DefaultTypeAdapters.BOOLEAN_AS_STRING)
                .registerTypeAdapter(boolean.class, DefaultTypeAdapters.BOOLEAN)
                .registerTypeAdapter(Integer.class, DefaultTypeAdapters.INTEGER)
                .registerTypeAdapter(int.class, DefaultTypeAdapters.INTEGER)
                .registerTypeAdapter(Long.class, DefaultTypeAdapters.LONG)
                .registerTypeAdapter(long.class, DefaultTypeAdapters.LONG)
                .registerTypeAdapter(Float.class, DefaultTypeAdapters.FLOAT)
                .registerTypeAdapter(float.class, DefaultTypeAdapters.FLOAT)
                .registerTypeAdapter(Double.class, DefaultTypeAdapters.DOUBLE)
                .registerTypeAdapter(double.class, DefaultTypeAdapters.DOUBLE)
                .registerTypeAdapter(Number.class, DefaultTypeAdapters.NUMBER)
                .registerTypeAdapter(String.class, DefaultTypeAdapters.STRING)
                .registerTypeAdapter(BigDecimal.class, DefaultTypeAdapters.BIG_DECIMAL)
                .serializeNulls() //不忽略为 null 的参数
                .create();
    }
}
