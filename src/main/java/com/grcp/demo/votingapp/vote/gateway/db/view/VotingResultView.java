package com.grcp.demo.votingapp.vote.gateway.db.view;

public record VotingResultView(long poolOptionId, String description, long totalVotes) {
}
