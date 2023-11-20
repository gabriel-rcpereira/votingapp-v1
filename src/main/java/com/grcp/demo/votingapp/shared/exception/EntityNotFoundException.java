package com.grcp.demo.votingapp.shared.exception;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(BaseErrorDetail errorDetail) {
        super(errorDetail);
    }
}
