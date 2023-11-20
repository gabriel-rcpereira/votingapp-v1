package com.grcp.demo.votingapp.pool.usecase;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.error.PoolError;
import com.grcp.demo.votingapp.pool.gateway.PoolGateway;
import com.grcp.demo.votingapp.shared.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        Pool pool = poolGateway.findPoolById(id)
                .orElseThrow(() -> new EntityNotFoundException(PoolError.POLL_NOT_FOUND));
        List<PoolOption> options = poolGateway.findPoolOptionsByPoolId(pool.id());
        return toPool(pool, options);
    }

    private static Pool toPool(Pool pool, List<PoolOption> options) {
        return new Pool(pool.id(), pool.description(), pool.expiredAt(), options);
    }
}
