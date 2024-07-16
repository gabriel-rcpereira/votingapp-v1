package com.grcp.demo.votingapp.vote.entrypoint.model;

import java.util.List;

public record AggregatedVotingResultResponseDto(
        long totalPoolVotes,
        List<VotingResultResponseDto> votingResults) {
}
