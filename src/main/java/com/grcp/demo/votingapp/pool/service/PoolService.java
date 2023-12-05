package com.grcp.demo.votingapp.pool.service;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.error.PoolError;
import com.grcp.demo.votingapp.pool.gateway.PoolGateway;
import com.grcp.demo.votingapp.shared.error.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@RequiredArgsConstructor
@Service
public class PoolService {

    private final PoolGateway poolGateway;

    public void createPool(@Valid Pool pool) {
        poolGateway.savePool(pool);
    }

    public void validatePoolExists(PoolId id) {
        fetchPool(id);
    }

    public Pool fetchPool(@Valid PoolId id) {
        return poolGateway.findPoolById(id)
                .orElseThrow(() -> new EntityNotFoundException(PoolError.POLL_NOT_FOUND));
    }
}
