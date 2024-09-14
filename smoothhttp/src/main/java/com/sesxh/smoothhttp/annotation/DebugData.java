package com.sesxh.smoothhttp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LYH
 * @date 2021/1/7
 * @time 9:35
 * @desc 模拟服务器返回数据，仅在debug模式有效，可以在接口未开发时模拟网络请求。
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DebugData {
    String json();
}
