package com.grcp.demo.votingapp.vote.entrypoint.model;

import jakarta.validation.constraints.NotNull;

public record VoteRequestDto(@NotNull Long poolOptionId) {
}
