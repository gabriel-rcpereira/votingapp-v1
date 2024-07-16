package com.grcp.demo.votingapp.pool.domain;

import com.grcp.demo.votingapp.pool.domain.error.PoolError;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PoolOption(
        @NotNull(message = "001.008")
        @Valid
        PoolOptionId id,
        @NotBlank(message = "001.002")
        String description,
        @NotNull(message = "001.009")
        @Valid
        PoolId poolId) {
}
