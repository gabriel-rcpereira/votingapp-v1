package com.grcp.demo.votingapp.pool.domain;

import com.github.f4b6a3.tsid.TsidCreator;
import org.springframework.util.Assert;

public record PoolId(Long value) {

    public PoolId {
        Assert.notNull(value, "");
    }

    public static PoolId asNew() {
        return new PoolId(TsidCreator.getTsid().toLong());
    }
}
