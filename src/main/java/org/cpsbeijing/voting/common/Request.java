package org.cpsbeijing.voting.common;

import java.io.Serializable;

public class Request<T> implements Serializable {
    private T param;

    public Request() {
    }

    public Request(T param) {
        this.param = param;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

}
