package com.grcp.demo.votingapp.vote.domain;

import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record Vote(
        @Valid @NotNull(message = "002.003") VoteId id,
        @Valid @NotNull(message = "002.004") PoolOptionId poolOptionId) {

    public Vote(PoolOptionId poolOptionId) {
        this(VoteId.asNew(), poolOptionId);
    }
}
