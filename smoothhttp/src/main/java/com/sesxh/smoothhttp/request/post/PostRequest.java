package com.sesxh.smoothhttp.request.post;

import com.sesxh.smoothhttp.MediaTypes;
import com.sesxh.smoothhttp.lifecycle.LifeCycle;
import com.sesxh.smoothhttp.request.BaseHttpRequest;
import com.sesxh.smoothhttp.transformer.RxScheduler;
import com.sesxh.smoothhttp.utils.GsonUtil;
import java.util.Map;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author LYH
 * @date 2021/1/12
 * @time 8:56
 * @desc Post请求基类
 **/
public class PostRequest extends BaseHttpRequest<PostRequest> {

    // url中带参数 post
    private StringBuilder urlSb = new StringBuilder();
    // body Post
    private RequestBody requestBody;
    // body post 参数类型
    private MediaType mediaType;
    // body post 内容
    private String content;
    //是否Query方式
    private boolean isQuery;
    // 强制使用form表单
    private boolean isMultipart;

    public PostRequest(String url) {
        super(url);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <K> Observable<K> execute(Class<K> clazz,boolean isList) {
        if (urlSb.length() != 0) {
            url = url + urlSb.toString();
        }
        return createObservable()
                .compose(LifeCycle.create().initSubject(mSubject).bindLife())
                .compose(cast(clazz,isList))
                .compose(RxScheduler.retrySync());
    }


    public PostRequest requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    /**
     * 设置 postBody 对象
     *
     * @return self
     */
    public PostRequest json() {
        return type(MediaTypes.APPLICATION_JSON_TYPE);
    }

    public PostRequest html() {
        return type(MediaTypes.TEXT_HTML_TYPE);
    }

    public PostRequest text() {
        return type(MediaTypes.TEXT_PLAIN_TYPE);
    }

    public PostRequest xml() {
        return type(MediaTypes.APPLICATION_XML_TYPE);
    }

    public PostRequest xmlText() {
        return type(MediaTypes.TEXT_XML_TYPE);
    }

    public void mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public PostRequest type( MediaType mediaType) {
        mediaType(mediaType);
        return this;
    }

    private RequestBody defaultBody() {
        json();
        return RequestBody.create(mediaType, content);
    }


    public PostRequest isQuery(boolean isQuery) {
        this.isQuery = isQuery;
        if(isQuery){
            isMultipart=false;
        }
        return this;
    }

    public PostRequest isMultipart(boolean isMultipart) {
        this.isMultipart = isMultipart;
        if(isMultipart){
            isQuery=false;
        }
        return this;
    }

    /**
     * post url 中添加参数
     *
     * @param paramKey   key
     * @param paramValue value
     * @return self
     */
    public PostRequest urlParams(String paramKey, String paramValue) {
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

    /**
     * post url 中添加参数
     *
     * @param datas source
     * @return self
     */


    public PostRequest urlParams(Map<String, Object> datas) {
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            urlParams(entry.getKey(), entry.getValue().toString());
        }
        return this;
    }

    private Observable<String> createObservable() {
        if (isQuery) {
            return mApi.postQuery(url, headers, params);
        }
        if (isMultipart) {
            return mApi.postForm(url, headers, params);
        }
        content=GsonUtil.gson().toJson(params);
        if (content != null && mediaType != null) {
            requestBody = RequestBody.create(mediaType, content);
        }
        if(requestBody==null) {
            requestBody = defaultBody();
        }
        return mApi.post(url, headers, requestBody);

    }

}
