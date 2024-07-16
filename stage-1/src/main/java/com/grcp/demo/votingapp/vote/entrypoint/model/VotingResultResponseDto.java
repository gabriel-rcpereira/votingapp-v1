package com.grcp.demo.votingapp.vote.entrypoint.model;

public record VotingResultResponseDto(
        Long optionId,
        String description,
        long votes,
        Double percentage) {
}