package com.grcp.demo.votingapp.pool.entrypoint.model;

import jakarta.validation.constraints.NotBlank;

public record PoolOptionRequestDto(
        @NotBlank
        String description) {
}
