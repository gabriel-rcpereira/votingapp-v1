package com.grcp.demo.votingapp.pool.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PoolOption(
        @NotNull
        @Valid
        PoolOptionId id,
        @NotBlank
        String description,
        @NotNull
        @Valid
        PoolId poolId) {

}
