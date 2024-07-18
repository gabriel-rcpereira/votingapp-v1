package com.grcp.demo.votingapp.vote.gateway.db.view;

public record VotingResultView(Long poolOptionId, String description, Integer totalVotes) {
}
