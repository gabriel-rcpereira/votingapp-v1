package com.grcp.demo.votingapp.pool.domain.error;

import com.grcp.demo.votingapp.shared.error.model.BaseErrorDetail;
import lombok.RequiredArgsConstructor;

import static com.grcp.demo.votingapp.shared.error.model.ModuleErrorCode.POOL_ERROR;

@RequiredArgsConstructor
public enum PoolError implements BaseErrorDetail {

    POLL_NOT_FOUND(POOL_ERROR.formatError("001"), "Pool not found"),
    INVALID_POLL_OPTION_DESCRIPTION(POOL_ERROR.formatError("002"), "Pool option description cannot be null");

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
