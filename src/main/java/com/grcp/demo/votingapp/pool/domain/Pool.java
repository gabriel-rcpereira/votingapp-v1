package com.grcp.demo.votingapp.pool.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record Pool(
        @NotNull
        @Valid
        PoolId id,
        @NotBlank
        String description,
        @NotNull
        @Future
        LocalDateTime expiredAt,
        @NotNull
        @Valid
        List<PoolOption> options) {

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    public boolean doesPoolOptionBelongToPool(PoolOptionId poolOptionId) {
        return options.stream()
                .anyMatch(poolOption -> poolOption.id().equals(poolOptionId));
    }
}
