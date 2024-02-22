package com.grcp.demo.votingapp.vote.entrypoint.api;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.entrypoint.model.AggregatedVotingResultResponseDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VoteRequestDto;
import com.grcp.demo.votingapp.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.grcp.demo.votingapp.vote.entrypoint.mapper.VoteDtoMapper.toAggregatedVotingResultResponseDto;
import static com.grcp.demo.votingapp.vote.entrypoint.mapper.VoteDtoMapper.toPoolId;
import static com.grcp.demo.votingapp.vote.entrypoint.mapper.VoteDtoMapper.toVote;

@RequiredArgsConstructor
@Validated
@RestController
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/api/v1/pools/{poolId}/votes")
    public ResponseEntity<Void> postNewVote(@PathVariable("poolId") Long poolId, @RequestBody VoteRequestDto request) {
        Vote newVote = toVote(request);
        PoolId typedPoolId = toPoolId(poolId);
        voteService.registerNewVote(typedPoolId, newVote);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/pools/{poolId}/votes")
    public ResponseEntity<AggregatedVotingResultResponseDto> getVotes(@PathVariable("poolId") Long poolId) {
        PoolId typePoolId = toPoolId(poolId);
        AggregatedVotingResult aggregatedVotingResult = voteService.fetchAggregatedVotingResult(typePoolId);
        AggregatedVotingResultResponseDto aggregatedVotingResultResponseDto = toAggregatedVotingResultResponseDto(
                aggregatedVotingResult);
        return ResponseEntity.ok(aggregatedVotingResultResponseDto);
    }
}
