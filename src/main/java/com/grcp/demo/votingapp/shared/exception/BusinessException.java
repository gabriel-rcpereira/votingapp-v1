package com.grcp.demo.votingapp.shared.exception;

public class BusinessException extends BaseException {

    public BusinessException(BaseErrorDetail errorDetail, Object... args) {
        super(errorDetail, args);
    }
}
