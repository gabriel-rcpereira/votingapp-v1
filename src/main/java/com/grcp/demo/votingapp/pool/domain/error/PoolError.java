package com.grcp.demo.votingapp.pool.domain.error;

import com.grcp.demo.votingapp.shared.exception.BaseErrorDetail;
import lombok.RequiredArgsConstructor;

import static com.grcp.demo.votingapp.shared.exception.ModuleErrorCode.POOL_ERROR;

@RequiredArgsConstructor
public enum PoolError implements BaseErrorDetail {

    POLL_NOT_FOUND(POOL_ERROR.formatError("001"), "Pool not found");

    private final String code;
    private final String message;

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
