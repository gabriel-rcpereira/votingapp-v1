package com.grcp.demo.votingapp.vote.domain;

import com.github.f4b6a3.tsid.TsidCreator;

public record VoteId(Long value) {

    public static VoteId asNew() {
        return new VoteId(TsidCreator.getTsid().toLong());
    }
}
