package com.sesxh.smoothhttp.utils;

/**
 * @author LYH
 * @date 2021/1/7
 * @time 19:10
 * @desc 类型强转
 */

public class CastUtils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

}
