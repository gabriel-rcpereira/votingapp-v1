package com.grcp.demo.votingapp.pool.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record Pool(
        @NotNull(message = "001.003")
        @Valid
        PoolId id,
        @NotBlank(message = "001.004")
        String description,
        @NotNull(message = "001.005")
        @Future(message = "001.006")
        LocalDateTime expiredAt,
        @NotNull(message = "001.007")
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
