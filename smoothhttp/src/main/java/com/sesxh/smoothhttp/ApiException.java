package com.sesxh.smoothhttp;

/**
 * @author LYH
 * @date 2020/12/28
 * @time 17:48
 * @desc
 **/

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import com.sesxh.smoothhttp.utils.CastUtils;

import android.net.ParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;


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
public class ApiException {


    private static final int CODE_PAGE_REDIRECTION = 302;
    private static final int CODE_RE_SUBMIT = 303;
    private static final int CODE_UNAUTHORIZED = 401;
    private static final int CODE_FORBIDDEN = 403;
    private static final int CODE_NOT_FOUND = 404;
    private static final int CODE_REQUEST_TIMEOUT = 408;
    private static final int CODE_INTERNAL_SERVER_ERROR = 500;
    private static final int CODE_SERVICE_UNAVAILABLE = 503;
    private static final String MESSAGE_PAGE_REDIRECTION = "页面重定向";
    private static final String MESSAGE_RE_SUBMIT = "重复提交";
    private static final String MESSAGE_UNAUTHORIZED ="认证异常";
    private static final String MESSAGE_FORBIDDEN = "操作未授权";
    private static final String MESSAGE_NOT_FOUND ="资源不存在";
    private static final String MESSAGE_REQUEST_TIMEOUT = "服务器执行超时";
    private static final String MESSAGE_INTERNAL_SERVER_ERROR ="服务器内部错误";
    private static final String MESSAGE_SERVICE_UNAVAILABLE = "服务器不可用";

    public static final int CODE_PARSE_ERROR = 1000;// 解析错误
    public static final int CODE_SSL_ERROR = 1001;//证书出错
    public static final int CODE_CONNECT_ERROR = 1002;//连接失败
    public static final int CODE_TIMEOUT_ERROR = 1003;//连接超时
    public static final int CODE_HOST_NOT_FOUND = 1004;//主机地址未知
    public static final int CODE_DATA_ERROR = 1005;//数据错误
    public static final int CODE_UNKNOWN = 1006;//未知错误

    public static final String MESSAGE_PARSE_ERROR = "解析错误";
    public static final String MESSAGE_SSL_ERROR = "证书验证失败";
    public static final String MESSAGE_CONNECT_ERROR = "连接失败";
    public static final String MESSAGE_TIMEOUT_ERROR = "连接超时";
    public static final String MESSAGE_HOST_NOT_FOUND = "主机地址未知";
    public static final String MESSAGE_UNKNOWN = "未知错误";//未知错误
    public static final String MESSAGE_NETWORK_ERROR = "网络错误";

    public static SmoothHttpThrowable handleException(Throwable e) {
        if(e instanceof SmoothHttpThrowable){
            return CastUtils.cast(e);
        }
        SmoothHttpThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex=SmoothHttpThrowable.create(httpException.code(),httpException);
            switch (httpException.code()) {
                case CODE_PAGE_REDIRECTION:
                    ex.msg(MESSAGE_PAGE_REDIRECTION);
                    break;
                case CODE_RE_SUBMIT:
                    ex.msg(MESSAGE_RE_SUBMIT);
                    break;
                case CODE_UNAUTHORIZED:
                    ex.msg(MESSAGE_UNAUTHORIZED);
                    break;
                case CODE_FORBIDDEN:
                    ex.msg(MESSAGE_FORBIDDEN);
                    break;
                case CODE_NOT_FOUND:
                    ex.msg(MESSAGE_NOT_FOUND);
                    break;
                case CODE_REQUEST_TIMEOUT:
                    ex.msg(MESSAGE_REQUEST_TIMEOUT);
                    break;
                case CODE_INTERNAL_SERVER_ERROR:
                    ex.msg(MESSAGE_INTERNAL_SERVER_ERROR);
                    break;
                case CODE_SERVICE_UNAVAILABLE:
                    ex.msg(MESSAGE_SERVICE_UNAVAILABLE);
                    break;
                default:
                    ex.msg(MESSAGE_NETWORK_ERROR);
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex=SmoothHttpThrowable.create(CODE_PARSE_ERROR,e);
            ex.msg(MESSAGE_PARSE_ERROR);
        } else if (e instanceof ConnectException) {
            ex=SmoothHttpThrowable.create(CODE_CONNECT_ERROR,e);
            ex.msg(MESSAGE_CONNECT_ERROR);
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex=SmoothHttpThrowable.create(CODE_SSL_ERROR,e);
            ex.msg(MESSAGE_SSL_ERROR);
        } else if (e instanceof ConnectTimeoutException||e instanceof java.net.SocketTimeoutException||e instanceof TimeoutException) {
            ex=SmoothHttpThrowable.create(CODE_TIMEOUT_ERROR,e);
            ex.msg(MESSAGE_TIMEOUT_ERROR);
        } else if (e instanceof java.net.UnknownHostException) {
            ex=SmoothHttpThrowable.create(CODE_HOST_NOT_FOUND,e);
            ex.msg(MESSAGE_HOST_NOT_FOUND);
        } else {
            ex=SmoothHttpThrowable.create(CODE_UNKNOWN,e);
            ex.msg(MESSAGE_UNKNOWN);
        }
        return ex;
    }



}
