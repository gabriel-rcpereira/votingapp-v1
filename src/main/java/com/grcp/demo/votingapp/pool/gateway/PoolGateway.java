package com.grcp.demo.votingapp.pool.gateway;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;

import java.util.List;
import java.util.Optional;

public interface PoolGateway {

    void savePool(Pool pool);

    Optional<Pool> findPoolById(PoolId id);

    List<PoolOption> findPoolOptionsByPoolId(PoolId id);
}
