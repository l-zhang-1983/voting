package org.cpsbeijing.voting.common;

import java.io.Serializable;

public class PagingRequest<T> extends Request<T> implements Serializable {
    private PagingConfig pagingConfig;

    public PagingRequest() {}

    public PagingRequest(PagingConfig pagingConfig) {
        super();
        this.pagingConfig = pagingConfig;
    }

    public PagingConfig getPagingConfig() {
        return pagingConfig;
    }

    public void setPagingConfig(PagingConfig pagingConfig) {
        this.pagingConfig = pagingConfig;
    }
}
