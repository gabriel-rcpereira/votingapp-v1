package com.grcp.demo.votingapp.vote.domain.error;

import com.grcp.demo.votingapp.shared.exception.BaseErrorDetail;
import com.grcp.demo.votingapp.shared.exception.ModuleErrorCode;
import lombok.RequiredArgsConstructor;

import static com.grcp.demo.votingapp.shared.exception.ModuleErrorCode.*;

@RequiredArgsConstructor
public enum VoteError implements BaseErrorDetail {

    EXPIRED_POLL(VOTE.formatError("001"), "Pool already expired"),
    OPTION_DOES_NOT_BELONG_TO_POOL(VOTE.formatError("002"), "Option does not belong to Pool %s");

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
