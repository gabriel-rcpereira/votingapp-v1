package com.grcp.demo.votingapp.shared.error.exception;

import com.grcp.demo.votingapp.shared.error.model.BaseErrorDetail;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(BaseErrorDetail errorDetail) {
        super(errorDetail);
    }
}
