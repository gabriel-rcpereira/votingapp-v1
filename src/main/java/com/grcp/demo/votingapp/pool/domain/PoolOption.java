package com.grcp.demo.votingapp.pool.domain;

public record PoolOption(
        PoolOptionId id,
        String description,
        PoolId poolId) {

}
