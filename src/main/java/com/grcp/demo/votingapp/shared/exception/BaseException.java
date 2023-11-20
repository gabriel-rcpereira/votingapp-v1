package com.grcp.demo.votingapp.shared.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final BaseErrorDetail errorDetail;
    private final Object[] args;

    public BaseException(BaseErrorDetail errorDetail, Object...args) {
        super(String.format(errorDetail.message(), args));
        this.errorDetail = errorDetail;
        this.args = args;
    }
}
