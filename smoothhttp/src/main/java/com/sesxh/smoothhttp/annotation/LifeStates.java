package com.sesxh.smoothhttp.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author LYH
 * @date 2021/1/26
 * @time 10:34
 * @desc 绑定生命周期
 **/

@IntDef({LifeStates.DESTROY, LifeStates.DESTROY_VIEW})
@Retention(RetentionPolicy.SOURCE)
public @interface LifeStates {

    public static final int DESTROY = 0;//Activity onDestroy
    public static final int DESTROY_VIEW = 1;//Fragment onDestroyView
}
