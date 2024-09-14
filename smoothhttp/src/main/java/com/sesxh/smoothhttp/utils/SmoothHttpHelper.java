package com.sesxh.smoothhttp.utils;

import java.lang.reflect.Type;

/**
 * @author LYH
 * @date 2021/8/13
 * @time 17:11
 * @desc
 **/
public class SmoothHttpHelper {

    // 判断是否是基本数据类型
    public static  boolean isBaseType(Type type){
        return type.equals(String.class) ||
                type.equals(Integer.class)||
                type.equals(Byte.class)||
                type.equals(Long.class) ||
                type.equals(Double.class) ||
                type.equals(Float.class)||
                type.equals(Character.class) ||
                type.equals(Short.class)||
                type.equals(Boolean.class);
    }

    
}
