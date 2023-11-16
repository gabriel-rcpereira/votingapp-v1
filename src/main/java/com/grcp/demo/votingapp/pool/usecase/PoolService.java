package com.grcp.demo.votingapp.pool.usecase;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.gateway.PoolGateway;
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

    @Transactional
    public void createPool(@Valid Pool pool) {
        Pool savedPool = poolGateway.savePool(pool);
        saveOptions(savedPool);
    }

    public void validatePoolExists(PoolId id) {
        fetchPool(id);
    }

    public Pool fetchPool(@Valid PoolId id) {
        Pool pool = poolGateway.findPoolById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        List<PoolOption> options = poolGateway.findPoolOptionsByPoolId(pool.id());
        return new Pool(pool.id(), pool.description(), pool.expiredAt(), options);
    }

    private void saveOptions(Pool savedPool) {
        List<PoolOption> optionsWithPoolId = updatePoolIdInOptions(savedPool);
        poolGateway.savePoolOptions(optionsWithPoolId);
    }

    private List<PoolOption> updatePoolIdInOptions(Pool savedPool) {
        return savedPool.options().stream()
                .map(option -> new PoolOption(option.id(), option.description(), savedPool.id()))
                .toList();
    }
}
