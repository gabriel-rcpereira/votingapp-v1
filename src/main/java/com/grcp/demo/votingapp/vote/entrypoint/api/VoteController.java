package com.grcp.demo.votingapp.vote.entrypoint.api;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.entrypoint.model.AggregatedVotingResultResponseDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VoteRequestDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VotingResultResponseDto;
import com.grcp.demo.votingapp.vote.usecase.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/api/v1/pools/{poolId}/votes")
    public ResponseEntity<?> postNewVote(
            @PathVariable("poolId") Long poolId,
            @RequestBody @Valid VoteRequestDto request) {
        PoolOptionId typedPoolOptionId = new PoolOptionId(request.poolOptionId());
        PoolId typedPoolId = new PoolId(poolId);
        Vote newVote = new Vote(typedPoolOptionId);
        voteService.registerNewVote(typedPoolId, newVote);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/pools/{poolId}/votes")
    public ResponseEntity<AggregatedVotingResultResponseDto> getVotes(@PathVariable("poolId") Long poolId) {
        PoolId typePoolId = new PoolId(poolId);
        AggregatedVotingResult aggregatedVotingResult = voteService.fetchAggregatedVotingResult(typePoolId);
        List<VotingResultResponseDto> votingResultsResponse = aggregatedVotingResult.votingResults().stream()
                .map(votingResult -> new VotingResultResponseDto(
                        votingResult.poolOptionId().value(),
                        votingResult.description(),
                        votingResult.totalVotes(),
                        votingResult.percentage()))
                .toList();
        return ResponseEntity.ok(
                new AggregatedVotingResultResponseDto(
                        aggregatedVotingResult.totalPoolVotes(),
                        votingResultsResponse));
    }
}
