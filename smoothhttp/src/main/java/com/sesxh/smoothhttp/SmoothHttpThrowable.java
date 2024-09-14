package com.sesxh.smoothhttp;

import androidx.annotation.Nullable;

import com.sesxh.smoothhttp.utils.CastUtils;
import java.io.IOException;

/**
 * @author LYH
 * @date 2020/12/28
 * @time 17:50
 * @desc 全局异常
 **/
public class SmoothHttpThrowable extends IOException {

    private int mCode; //错误码。
    private int mFlag; // 标记，用来区分每个异常。
    private String mMsg; //错误信息。
    private Object mData; //附加字段。

    public static SmoothHttpThrowable create(int code,Throwable throwable){
        if(throwable==null){
            throwable=new SmoothHttpException(ApiException.MESSAGE_UNKNOWN);
        }
        return new SmoothHttpThrowable(code,throwable);
    }

    public static SmoothHttpThrowable create(int code,String message){
        return new SmoothHttpThrowable(code,message);
    }

    protected SmoothHttpThrowable(int code,Throwable throwable) {
        super(throwable);
        this.mCode = code;
        this.mMsg=throwable.getLocalizedMessage();
    }

    protected SmoothHttpThrowable(int code,String message) {
        super(new SmoothHttpException(message));
        this.mCode = code;
        this.mMsg=message;
    }

    public SmoothHttpThrowable msg(String msg) {
        this.mMsg=msg;
        return this;
    }


    public SmoothHttpThrowable flag(int flag) {
        mFlag = flag;
        return this;
    }

    public SmoothHttpThrowable data(Object data) {
        mData = data;
        return this;
    }

    public int getCode() {
        return mCode;
    }

    public int getFlag() {
        return mFlag;
    }

    public <T> T getData() {
        if(mData==null){
            return null;
        }
        return CastUtils.cast(mData);
    }

    @Nullable
    @Override
    public String getMessage() {
        return mMsg;
    }

    @Override
    public String toString() {
        return "SmoothHttpThrowable{" +
                "Code=" + mCode +
                ", Flag=" + mFlag +
                ", Message='" + getLocalizedMessage() + '\'' +
                ", Throwable=" + getCause() +
                ", Data=" + mData +
                '}';
    }

    public static SmoothHttpThrowable unknown (){
        return create(ApiException.CODE_UNKNOWN,ApiException.MESSAGE_UNKNOWN);
    }

    public static SmoothHttpThrowable unknown (Throwable t){
        return create(ApiException.CODE_UNKNOWN,t);
    }

    private static class SmoothHttpException extends RuntimeException{

        public SmoothHttpException(String message) {
            super(message);
        }

    }
}
