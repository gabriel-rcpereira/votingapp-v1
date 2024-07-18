package com.grcp.demo.votingapp.vote.domain;

import com.grcp.demo.votingapp.pool.domain.PoolOptionId;

public record VotingResult(
        PoolOptionId poolOptionId,
        String description,
        long totalVotes,
        Double percentage) {
}
