package com.sesxh.smoothhttp.transformer;

import androidx.annotation.NonNull;

/**
 * @author LYH
 * @date 2021/1/13
 * @time 13:43
 * @desc 将Response原始数据返回，不做任何解析
 */


@SuppressWarnings("unChecked")
public class ResponseStringConverter extends DefaultConverter<String> {


    public ResponseStringConverter() {

    }

    @Override
    public String apply(@NonNull String json) {
        return json;
    }
}
