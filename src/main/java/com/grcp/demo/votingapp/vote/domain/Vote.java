package com.grcp.demo.votingapp.vote.domain;

import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record Vote(@Valid @NotNull VoteId id, @Valid @NotNull PoolOptionId poolOptionId) {

    public Vote(PoolOptionId poolOptionId) {
        this(VoteId.asNew(), poolOptionId);
    }
}
