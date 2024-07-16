package com.grcp.demo.votingapp.shared.error.exception;

import com.grcp.demo.votingapp.shared.error.model.BaseErrorDetail;
import lombok.Getter;

public abstract class BaseException extends RuntimeException {

    private final BaseErrorDetail errorDetail;

    private final Object[] args;

    public BaseException(BaseErrorDetail errorDetail, Object...args) {
        super(errorDetail.code());
        this.errorDetail = errorDetail;
        this.args = args;
    }

    public String code() {
        return errorDetail.code();
    }

    public Object[] args() {
        return args;
    }
}
