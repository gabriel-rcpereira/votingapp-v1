package com.grcp.demo.votingapp.pool.fixture;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;

import java.time.LocalDateTime;
import java.util.List;

public class PoolFixture {
    public static Pool validPool(PoolOptionId votedPoolOptionId, PoolId poolId) {
        List<PoolOption> mockedPoolOptions = validPoolOptions(votedPoolOptionId, poolId);
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(15);
        return new Pool(
                poolId,
                "valid pool",
                expiredAt,
                mockedPoolOptions);
    }

    public static Pool expiredPool(PoolOptionId votedPoolOptionId, PoolId poolId) {
        List<PoolOption> mockedPoolOptions = validPoolOptions(votedPoolOptionId, poolId);
        LocalDateTime expiredAt = LocalDateTime.now().minusSeconds(1);
        return new Pool(
                poolId,
                "expired pool",
                expiredAt,
                mockedPoolOptions);
    }

    public static List<PoolOption> validPoolOptions(PoolOptionId votedPoolOptionId, PoolId poolId) {
        return List.of(
                new PoolOption(votedPoolOptionId, "option 1", poolId),
                new PoolOption(PoolOptionId.asNew(), "option 2", poolId),
                new PoolOption(PoolOptionId.asNew(), "option 3", poolId));
    }
}
