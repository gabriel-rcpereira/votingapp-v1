package com.grcp.demo.votingapp.vote.external.db.adapter;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;
import com.grcp.demo.votingapp.vote.service.VoteAdapter;
import com.grcp.demo.votingapp.vote.external.db.entity.VoteEntity;
import com.grcp.demo.votingapp.vote.external.db.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class VoteAdapterImpl implements VoteAdapter {

    private final VoteRepository voteRepository;

    @Override
    public void saveVote(Vote vote) {
        VoteEntity newVoteEntity = VoteEntity.builder()
                .id(vote.id().value())
                .poolOptionId(vote.poolOptionId().value())
                .build();
        voteRepository.save(newVoteEntity);
    }

    @Override
    public List<PoolOptionVotingResult> findPoolOptionVotingResultsByPoolId(PoolId poolId) {
        return voteRepository.findByPoolId(poolId.value()).stream()
                .map(votingResultView -> new PoolOptionVotingResult(
                        new PoolOptionId(votingResultView.poolOptionId()),
                        votingResultView.description(),
                        votingResultView.totalVotes()))
                .toList();
    }
}
