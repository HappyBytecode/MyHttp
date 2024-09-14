package com.sesxh.smoothhttp.convert;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.sesxh.smoothhttp.HttpGlobalConfig;
import com.sesxh.smoothhttp.utils.GsonUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author LYH
 * @date 2021/1/13
 * @time 8:58
 * @desc 默认自定义解析工厂类
 */

public class DefaultConvertFactory extends Converter.Factory {

    private com.google.gson.Gson gson;

    public static DefaultConvertFactory create() {
        return create(GsonUtil.gson());
    }

    private static DefaultConvertFactory create(@NonNull com.google.gson.Gson gson) {
        return new DefaultConvertFactory(gson);
    }

    private DefaultConvertFactory(com.google.gson.Gson gson) {
        this.gson = gson;
    }

    /**
     * 这里这个 type 是本地接口方法中传入的泛型类型
     *
     * @param type        Retrofit 接口传入的泛型对象
     * @param annotations Retrofit 接口的注解
     * @param retrofit    Retrofit 对象
     * @return 转换器
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new DefaultResponseBodyConverter();

    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new RequestBodyConverter<>(gson, adapter);
    }

}
