package com.sesxh.smoothhttp.request.io;


import com.sesxh.smoothhttp.MediaTypes;
import com.sesxh.smoothhttp.SmoothHttp;
import com.sesxh.smoothhttp.callbacks.TransmitCallback;
import com.sesxh.smoothhttp.callbacks.UploadCallBack;
import com.sesxh.smoothhttp.interceptor.UploadInterceptor;
import com.sesxh.smoothhttp.lifecycle.LifeCycle;
import com.sesxh.smoothhttp.request.post.PostRequest;
import com.sesxh.smoothhttp.transformer.RxScheduler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @author LYH
 * @date 2021/1/12
 * @time 20:08
 * @desc 上传文件
 */

public class UploadRequest extends IORequest<UploadRequest> {


    private List<MultipartBody.Part> multipartBodyParts = new ArrayList<>();
    private StringBuilder urlSb = new StringBuilder();

    public UploadRequest(String url) {
        super(url);

    }

    @Override
    protected <T extends TransmitCallback> void execute(T t) {
        UploadCallBack callback;
        if (t instanceof UploadCallBack) {
            callback = (UploadCallBack) t;
        } else {
            return;
        }
        if (urlSb.length() > 0) {
            url = url + urlSb.toString();
        }
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, Object>> entryIterator = params.entrySet().iterator();
            Map.Entry<String, Object> entry;
            while (entryIterator.hasNext()) {
                entry = entryIterator.next();
                if (entry != null&&entry.getValue()!=null) {
                    multipartBodyParts.add(MultipartBody.Part.createFormData(entry.getKey(), entry.getValue().toString()));
                }
            }
        }
        mApi.uploadFiles(url, multipartBodyParts)
                .compose(LifeCycle.create().initSubject(mSubject).bindLife())
                .compose(RxScheduler.io())
                .subscribe(callback);
    }



    /**
     * url 中添加参数
     *
     * @param datas source
     * @return self
     */


    public UploadRequest urlParams(Map<String, Object> datas) {
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            urlParams(entry.getKey(), entry.getValue().toString());
        }
        return this;
    }


    /**
     * url 中添加参数
     *
     * @param paramKey   key
     * @param paramValue value
     * @return self
     */
    public UploadRequest urlParams(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            if (urlSb.length() == 0) {
                urlSb.append("?");
            } else {
                urlSb.append("&");
            }
            urlSb.append(paramKey).append("=").append(paramValue);
        }
        return this;
    }


    public UploadRequest files(Map<String, File> fileMap) {
        if (fileMap == null) {
            return this;
        }
        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            file(entry.getKey(), entry.getValue());
        }
        return this;
    }


    public UploadRequest file(String key, File file) {
        if (key == null || file == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        this.multipartBodyParts.add(part);
        return this;
    }

    public UploadRequest image(String key, File file) {
        if (key == null || file == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        this.multipartBodyParts.add(part);
        return this;
    }

    public UploadRequest bytes(String key, byte[] bytes, String name) {
        if (key == null || bytes == null || name == null) {
            return this;
        }
        RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
        this.multipartBodyParts.add(part);
        return this;
    }

    public UploadRequest stream(String key, InputStream inputStream, String name) {
        if (key == null || inputStream == null || name == null) {
            return this;
        }

        RequestBody requestBody = create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, name, requestBody);
        this.multipartBodyParts.add(part);
        return this;
    }

    protected RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(@NonNull BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    public void request(UploadCallBack callback) {
        netInterceptor(new UploadInterceptor(callback));
        injectLocalParams();
        execute(callback);
    }

}
