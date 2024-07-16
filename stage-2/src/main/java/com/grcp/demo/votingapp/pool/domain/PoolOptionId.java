package com.grcp.demo.votingapp.pool.domain;

import com.github.f4b6a3.tsid.TsidCreator;
import jakarta.validation.constraints.NotNull;

public record PoolOptionId(@NotNull(message = "001.011") Long value) {

    public static PoolOptionId asNew() {
        return new PoolOptionId(TsidCreator.getTsid().toLong());
    }
}
