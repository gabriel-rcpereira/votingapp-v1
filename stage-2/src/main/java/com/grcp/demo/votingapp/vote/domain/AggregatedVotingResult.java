package com.grcp.demo.votingapp.vote.domain;

import java.util.List;

public record AggregatedVotingResult(long totalPoolVotes, List<VotingResult> votingResults) {
}
