package com.sesxh.smoothhttp.transformer;

import androidx.annotation.NonNull;
import com.google.gson.JsonParseException;
import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;
import com.sesxh.smoothhttp.entity.BaseResponse;
import com.sesxh.smoothhttp.utils.CastUtils;
import com.sesxh.smoothhttp.utils.GsonUtil;
import com.sesxh.smoothhttp.utils.SmoothHttpHelper;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;


/**
 * @author LYH
 * @date 2021/1/13
 * @time 13:43
 * @desc
 */


@SuppressWarnings("unChecked")
public class DefaultConverter<T> implements Function<String, T> {

    private Type type;

    public DefaultConverter() {

    }

    public void type(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public T apply(@NonNull String json) throws Exception {
        try{
            BaseResponse<T> baseResponse = GsonUtil.gson().fromJson(json, (Type)BaseResponse.class);
            String data = baseResponse.getData() == null ? "" : GsonUtil.gson().toJson(baseResponse.getData());
            if (baseResponse.isSuccess()) {
                if(SmoothHttpHelper.isBaseType(type)){
                    return CastUtils.cast(data);
                }
                return GsonUtil.gson().fromJson(data,type);
            } else {
                throw SmoothHttpThrowable.create(baseResponse.getStatusCode(),new IllegalArgumentException(baseResponse.getMessage()));
            }
        }catch (JsonParseException e){
            throw SmoothHttpThrowable.create(ApiException.CODE_PARSE_ERROR,e);
        }
    }


}
