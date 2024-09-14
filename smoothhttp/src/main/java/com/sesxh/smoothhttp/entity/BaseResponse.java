package com.sesxh.smoothhttp.entity;

/**
 * @auther LYH
 * @date 2020/11/10
 * @time 11:54
 * @desc 服务器返回的通用基类
 **/
public class BaseResponse<T> {


    /** 异常交互状态码
     * 200 成功
     * 300 业务异常
     * 301 登录超时
     * 302 页面重定向
     * 303 重复提交
     * 401 认证异常
     * 403 无权限
     * 500 服务异常
     */

    public static final int CODE_OK = 200;
    public static final int BUSINESS_ERROR = 300;
    public static final int lOGIN_TIMEOUT = 301;
    public static final int PAGE_REDIRECTION = 302;
    public static final int RE_SUBMIT = 303;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int INTERNAL_SERVER_ERROR = 500;


    /**
     * data : {"address":"","age":"","balance":"","birthday":"","idcardNo":"","name":"","outpatientNum":"","patId":"","phone":"","sex":""}
     * errorCode :
     * message :
     * statusCode : 0
     * token :
     */
    private T data;
    private String errorCode;
    private String message;
    private Integer statusCode;
    private String token;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess(){
        return CODE_OK==statusCode;
    }

}
