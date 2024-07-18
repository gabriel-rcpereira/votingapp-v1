package com.grcp.demo.votingapp.vote.domain;

import com.grcp.demo.votingapp.pool.domain.PoolOptionId;

public record Vote(VoteId id, PoolOptionId poolOptionId) {

    public Vote(PoolOptionId poolOptionId) {
        this(VoteId.asNew(), poolOptionId);
    }
}
