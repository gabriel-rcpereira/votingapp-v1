package com.grcp.demo.votingapp.vote.service;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;

import java.util.List;

public interface VoteAdapter {

    void saveVote(Vote vote);

    List<PoolOptionVotingResult> findPoolOptionVotingResultsByPoolId(PoolId poolId);
}
