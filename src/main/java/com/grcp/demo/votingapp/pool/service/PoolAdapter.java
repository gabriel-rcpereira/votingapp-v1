package com.grcp.demo.votingapp.pool.service;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;

import java.util.Optional;

public interface PoolAdapter {

    void savePool(Pool pool);

    Optional<Pool> findPoolById(PoolId id);
}
