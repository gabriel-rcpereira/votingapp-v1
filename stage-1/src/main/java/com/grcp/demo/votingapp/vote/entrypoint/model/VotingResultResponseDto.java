package com.grcp.demo.votingapp.vote.entrypoint.model;

public record VotingResultResponseDto(
        Long optionId,
        String description,
        Integer votes,
        Double percentage) {
}