package org.cpsbeijing.voting.common;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private String responseCode;
    private String responseMessage;
    private T data;
    private boolean success;

    public Response() {
        this.success = Boolean.TRUE;
        this.responseCode = "0";
        this.responseMessage = "请求成功";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
