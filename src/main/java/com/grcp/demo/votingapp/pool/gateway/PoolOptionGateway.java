package com.grcp.demo.votingapp.pool.gateway;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;

import java.util.List;

public interface PoolOptionGateway {

    void save(List<PoolOption> options);

    List<PoolOption> findByPoolId(PoolId id);
}
