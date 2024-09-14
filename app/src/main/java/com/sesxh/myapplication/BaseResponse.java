package com.sesxh.myapplication;

/**
 * @author LYH
 * @date 2020/12/28
 * @time 17:54
 * @desc
 **/
public class BaseResponse<T> {


    /**
     * resultcode : 200
     * reason : Return Successd!
     * result : {"province":"山东","city":"青岛","areacode":"0532","zip":"266000","company":"移动","card":""}
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private T result;
    private Integer error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public boolean isSuccess() {
        return 0==error_code;
    }

}
