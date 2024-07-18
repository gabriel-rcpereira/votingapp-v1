package com.grcp.demo.votingapp.vote.mapper;

import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;
import com.grcp.demo.votingapp.vote.domain.VotingResult;

import java.util.List;

public class VoteMapper {
    public static VotingResult toVotingResult(
            PoolOptionVotingResult poolOptionVotingResult,
            Double sharedPoolOptionPercentage) {
        return new VotingResult(
                poolOptionVotingResult.poolOptionId(),
                poolOptionVotingResult.description(),
                poolOptionVotingResult.totalVotes(),
                sharedPoolOptionPercentage);
    }

    public static AggregatedVotingResult toAggregatedVotingResult(
            long totalPoolVotes,
            List<VotingResult> votingResults) {
        return new AggregatedVotingResult(totalPoolVotes, votingResults);
    }
}
