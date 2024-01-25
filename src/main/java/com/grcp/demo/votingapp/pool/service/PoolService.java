package com.grcp.demo.votingapp.pool.service;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.error.PoolError;
import com.grcp.demo.votingapp.shared.error.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@RequiredArgsConstructor
@Service
public class PoolService {

    private final PoolAdapter poolAdapter;

    public void createPool(@Valid Pool pool) {
        poolAdapter.savePool(pool);
    }

    public void validatePoolExists(@Valid PoolId id) {
        fetchPool(id);
    }

    public Pool fetchPool(@Valid PoolId id) {
        return poolAdapter.findPoolById(id)
                .orElseThrow(() -> new EntityNotFoundException(PoolError.POLL_NOT_FOUND));
    }
}
