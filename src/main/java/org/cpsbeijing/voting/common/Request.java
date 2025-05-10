package org.cpsbeijing.voting.common;

import java.io.Serializable;

public class Request<T> implements Serializable {
    private T param;
    private PagingConfig pagingConfig;

    public Request(T param, PagingConfig pagingConfig) {
        this.param = param;
        this.pagingConfig = pagingConfig;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public PagingConfig getPagingConfig() {
        return pagingConfig;
    }

    public void setPagingConfig(PagingConfig pagingConfig) {
        this.pagingConfig = pagingConfig;
    }
}
