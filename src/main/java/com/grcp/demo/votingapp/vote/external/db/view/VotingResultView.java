package com.grcp.demo.votingapp.vote.external.db.view;

public record VotingResultView(long poolOptionId, String description, long totalVotes) {
}
