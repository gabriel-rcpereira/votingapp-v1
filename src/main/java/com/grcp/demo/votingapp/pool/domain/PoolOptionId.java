package com.grcp.demo.votingapp.pool.domain;

import com.github.f4b6a3.tsid.TsidCreator;

public record PoolOptionId(Long value) {

    public static PoolOptionId asNew() {
        return new PoolOptionId(TsidCreator.getTsid().toLong());
    }
}
