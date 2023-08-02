package com.grcp.demo.votingapp.vote.gateway;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;

import java.util.List;

public interface VoteGateway {

    void saveVote(Vote vote);

    List<PoolOptionVotingResult> findPoolOptionVotingResultsByPoolId(PoolId poolId);
}
