package com.grcp.demo.votingapp.pool.usecase;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.gateway.PoolGateway;
import com.grcp.demo.votingapp.pool.gateway.PoolOptionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PoolService {

    private final PoolGateway poolGateway;

    private final PoolOptionGateway poolOptionGateway;

    @Transactional
    public void createPool(Pool pool) {
        Pool savedPool = poolGateway.save(pool);
        saveOptions(savedPool);
    }

    public void validatePoolExists(PoolId id) {
        fetchPool(id);
    }

    public Pool fetchPool(PoolId id) {
        Pool pool = poolGateway.findById(id.value())
                .orElseThrow(() -> new RuntimeException("Not found"));
        List<PoolOption> options = poolOptionGateway.findByPoolId(pool.id());
        return new Pool(pool.id(), pool.description(), pool.expiredAt(), options);
    }

    private void saveOptions(Pool savedPool) {
        List<PoolOption> optionsWithPoolId = updatePoolIdInOptions(savedPool);
        poolOptionGateway.save(optionsWithPoolId);
    }

    private List<PoolOption> updatePoolIdInOptions(Pool savedPool) {
        return savedPool.options().stream()
                .map(option -> new PoolOption(option.id(), option.description(), savedPool.id()))
                .toList();
    }
}
