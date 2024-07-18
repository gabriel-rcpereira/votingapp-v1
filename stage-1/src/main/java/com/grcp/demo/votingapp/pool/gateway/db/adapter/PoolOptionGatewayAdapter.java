package com.grcp.demo.votingapp.pool.gateway.db.adapter;

import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.pool.gateway.PoolOptionGateway;
import com.grcp.demo.votingapp.pool.gateway.db.entity.PoolOptionEntity;
import com.grcp.demo.votingapp.pool.gateway.db.repository.PoolOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PoolOptionGatewayAdapter implements PoolOptionGateway {

    private final PoolOptionRepository poolOptionRepository;

    @Override
    public void save(List<PoolOption> options) {
        List<PoolOptionEntity> poolOptionsEntity = toEntities(options);
        poolOptionRepository.saveAll(poolOptionsEntity);
    }

    @Override
    public List<PoolOption> findByPoolId(PoolId id) {
        return poolOptionRepository.findByPoolId(id.value()).stream()
                .map(PoolOptionGatewayAdapter::toDomain)
                .toList();
    }

    private static PoolOption toDomain(PoolOptionEntity poolOptionEntity) {
        return new PoolOption(
                new PoolOptionId(poolOptionEntity.getId()),
                poolOptionEntity.getDescription(),
                new PoolId(poolOptionEntity.getPoolId()));
    }

    private static List<PoolOptionEntity> toEntities(List<PoolOption> options) {
        return options.stream()
                .map(option -> PoolOptionEntity.builder()
                        .id(option.id().value())
                        .description(option.description())
                        .poolId(option.poolId().value())
                        .build())
                .toList();
    }
}
