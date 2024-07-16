package com.grcp.demo.votingapp.shared.error.exception;

import com.grcp.demo.votingapp.shared.error.model.BaseErrorDetail;

public class BusinessException extends BaseException {

    public BusinessException(BaseErrorDetail errorDetail, Object...args) {
        super(errorDetail, args);
    }
}
