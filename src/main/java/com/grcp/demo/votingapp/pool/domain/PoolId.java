package com.grcp.demo.votingapp.pool.domain;

import com.github.f4b6a3.tsid.TsidCreator;
import jakarta.validation.constraints.NotNull;

public record PoolId(@NotNull Long value) {

    public static PoolId asNew() {
        return new PoolId(TsidCreator.getTsid().toLong());
    }
}
