package com.grcp.demo.votingapp.vote.fixture;

import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.vote.domain.Vote;

public class VoteFixture {
    public static Vote validVote(PoolOptionId votedPoolOptionId) {
        return new Vote(votedPoolOptionId);
    }
}
