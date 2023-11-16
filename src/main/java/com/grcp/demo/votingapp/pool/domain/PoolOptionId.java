package com.grcp.demo.votingapp.pool.domain;

import com.github.f4b6a3.tsid.TsidCreator;
import jakarta.validation.constraints.NotNull;

public record PoolOptionId(@NotNull Long value) {

    public static PoolOptionId asNew() {
        return new PoolOptionId(TsidCreator.getTsid().toLong());
    }
}
