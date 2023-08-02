package com.grcp.demo.votingapp.pool.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Pool(
        PoolId id,
        String description,
        LocalDateTime expiredAt,
        List<PoolOption> options) {

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    public boolean doesPoolOptionBelongToPool(PoolOptionId poolOptionId) {
        return options.stream()
                .anyMatch(poolOption -> poolOption.id().equals(poolOptionId));
    }
}
