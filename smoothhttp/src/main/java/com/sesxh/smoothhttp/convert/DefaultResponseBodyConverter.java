package com.sesxh.smoothhttp.convert;

import com.sesxh.smoothhttp.ApiException;
import com.sesxh.smoothhttp.SmoothHttpThrowable;
import java.io.IOException;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author LYH
 * @date 2021/1/13
 * @time 10:10
 * @desc 默认解析类
 */
  

public class DefaultResponseBodyConverter implements Converter<ResponseBody, String> {



    public DefaultResponseBodyConverter() {

    }


    @Override
    public String convert(@NonNull ResponseBody value) throws IOException {
        String response = value.string();
        try {
            return response;
        } catch (Exception e) {
            throw SmoothHttpThrowable.create(ApiException.CODE_PARSE_ERROR,"服务器返回数据异常！");
        } finally {
            value.close();
        }
    }


}
