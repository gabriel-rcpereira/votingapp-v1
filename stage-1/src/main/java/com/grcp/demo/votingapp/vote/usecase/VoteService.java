package com.grcp.demo.votingapp.vote.usecase;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.usecase.PoolService;
import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.domain.VotingResult;
import com.grcp.demo.votingapp.vote.gateway.VoteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteService {

    private static final double PERCENTAGE_BASE = 100.0;

    private final VoteGateway voteGateway;
    private final PoolService poolService;

    public void registerNewVote(PoolId poolId, Vote vote) {
        Pool pool = poolService.fetchPool(poolId);
        if (pool.isExpired()) {
            throw new RuntimeException("Pool already expired");
        }

        if (!pool.doesPoolOptionBelongToPool(vote.poolOptionId())) {
            throw new RuntimeException("Option does not belong to Pool %s".formatted(poolId));
        }

        voteGateway.saveVote(vote);
    }

    public AggregatedVotingResult fetchAggregatedVotingResult(PoolId poolId) {
        poolService.validatePoolExists(poolId);
        List<PoolOptionVotingResult> poolOptionResults = voteGateway.findPoolOptionVotingResultsByPoolId(
                poolId);
        long totalPoolVotes = calculateTotalPoolVotes(poolOptionResults);
        List<VotingResult> votingResults = computeVotingResults(totalPoolVotes, poolOptionResults);
        return toAggregatedVotingResult(totalPoolVotes, votingResults);
    }

    private List<VotingResult> computeVotingResults(
            long totalPoolVotes,
            List<PoolOptionVotingResult> poolOptionVotingResults) {
        return poolOptionVotingResults.stream()
                .map(poolOptionVotingResult -> computeVotingResult(
                        totalPoolVotes,
                        poolOptionVotingResult))
                .toList();
    }

    private VotingResult computeVotingResult(
            long totalPoolVotes,
            PoolOptionVotingResult poolOptionVotingResult) {
        Double sharedPoolOptionPercentage = calculateSharedPoolOptionPercentage(
                totalPoolVotes,
                poolOptionVotingResult.totalVotes());
        return toVotingResult(poolOptionVotingResult, sharedPoolOptionPercentage);
    }

    private VotingResult toVotingResult(
            PoolOptionVotingResult poolOptionVotingResult,
            Double sharedPoolOptionPercentage) {
        return new VotingResult(
                poolOptionVotingResult.poolOptionId(),
                poolOptionVotingResult.description(),
                poolOptionVotingResult.totalVotes(),
                sharedPoolOptionPercentage);
    }

    private AggregatedVotingResult toAggregatedVotingResult(
            long totalPoolVotes,
            List<VotingResult> votingResults) {
        return new AggregatedVotingResult(totalPoolVotes, votingResults);
    }

    private long calculateTotalPoolVotes(List<PoolOptionVotingResult> poolOptionVotingResults) {
        return poolOptionVotingResults.stream()
                .map(PoolOptionVotingResult::totalVotes)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private Double calculateSharedPoolOptionPercentage(long totalPoolVotes, Integer totalVotes) {
        return BigDecimal.valueOf((totalVotes * PERCENTAGE_BASE) / totalPoolVotes)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
