package com.grcp.demo.votingapp.pool.gateway;

import com.grcp.demo.votingapp.pool.domain.Pool;

import java.util.Optional;

public interface PoolGateway {

    Pool save(Pool pool);

    Optional<Pool> findById(Long id);
}
