package com.grcp.demo.votingapp.vote.usecase;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.usecase.PoolService;
import com.grcp.demo.votingapp.shared.exception.BusinessException;
import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.domain.VotingResult;
import com.grcp.demo.votingapp.vote.domain.error.VoteError;
import com.grcp.demo.votingapp.vote.gateway.VoteGateway;
import com.grcp.demo.votingapp.vote.mapper.VoteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Validated
@RequiredArgsConstructor
@Service
public class VoteService {

    private static final double PERCENTAGE_BASE = 100.0;
    private static final int MIN_NUMBER_TO_DIVIDE = 1;

    private final VoteGateway voteGateway;
    private final PoolService poolService;

    public void registerNewVote(@Valid PoolId poolId, @Valid Vote vote) {
        Pool pool = poolService.fetchPool(poolId);
        if (pool.isExpired()) {
            throw new BusinessException(VoteError.EXPIRED_POLL);
        }

        if (!pool.doesPoolOptionBelongToPool(vote.poolOptionId())) {
            throw new BusinessException(VoteError.OPTION_DOES_NOT_BELONG_TO_POOL, poolId.value());
        }

        voteGateway.saveVote(vote);
    }

    public AggregatedVotingResult fetchAggregatedVotingResult(@Valid PoolId poolId) {
        poolService.validatePoolExists(poolId);
        List<PoolOptionVotingResult> poolOptionResults = voteGateway.findPoolOptionVotingResultsByPoolId(
                poolId);
        long totalPoolVotes = calculateTotalPoolVotes(poolOptionResults);
        List<VotingResult> votingResults = computeVotingResults(totalPoolVotes, poolOptionResults);
        return VoteMapper.toAggregatedVotingResult(totalPoolVotes, votingResults);
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
        return VoteMapper.toVotingResult(poolOptionVotingResult, sharedPoolOptionPercentage);
    }

    private long calculateTotalPoolVotes(List<PoolOptionVotingResult> poolOptionVotingResults) {
        return poolOptionVotingResults.stream()
                .map(PoolOptionVotingResult::totalVotes)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private Double calculateSharedPoolOptionPercentage(long totalPoolVotes, Integer totalVotes) {
        return BigDecimal.valueOf((totalVotes * PERCENTAGE_BASE) / Math.max(MIN_NUMBER_TO_DIVIDE, totalPoolVotes))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
