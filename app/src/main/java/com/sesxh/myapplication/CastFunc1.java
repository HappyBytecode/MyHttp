package com.sesxh.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;
import com.sesxh.smoothhttp.log.Logger;
import com.sesxh.smoothhttp.transformer.DefaultConverter;
import com.sesxh.smoothhttp.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.List;


/**
 * @author LYH
 * @date 2021/1/13
 * @time 20:05
 * @desc
 **/
public class CastFunc1<T> extends DefaultConverter<T> {
    public CastFunc1() {

    }

    @Override
    public T apply(@NonNull String json) throws Exception {
        if (json.isEmpty()) {
            if (String.class.equals(getType())) {
                return (T) json;
            } else {
                throw SmoothHttpThrowable.create(ApiException.CODE_PARSE_ERROR,new JsonParseException("JSON数据为空！"));
            }
        } else {
            BaseResponse<T> baseResponse = GsonUtil.gson().fromJson(json, (Type)BaseResponse.class);
            if (baseResponse.getError_code() == null) {
                throw SmoothHttpThrowable.create(ApiException.CODE_DATA_ERROR,new IllegalArgumentException("服务器返回statusCode为null"));
            } else {
                String data = baseResponse.getResult() == null ? "" : GsonUtil.gson().toJson(baseResponse.getResult());
                if (baseResponse.isSuccess()) {
                    try {
                        return GsonUtil.gson().fromJson(data,getType());
                    } catch (Exception e) {
                        Logger.d("Convert",e.getMessage());
                        throw SmoothHttpThrowable.create(ApiException.CODE_PARSE_ERROR,e);
                    }

                } else {
                    throw SmoothHttpThrowable.create(ApiException.CODE_DATA_ERROR,new IllegalArgumentException("服务器返回statusCode不是成功的"));
                }
            }
        }
    }
}
