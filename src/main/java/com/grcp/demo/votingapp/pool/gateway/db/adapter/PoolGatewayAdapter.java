package com.grcp.demo.votingapp.pool.gateway.db.adapter;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.pool.gateway.PoolGateway;
import com.grcp.demo.votingapp.pool.gateway.db.entity.PoolEntity;
import com.grcp.demo.votingapp.pool.gateway.db.entity.PoolOptionEntity;
import com.grcp.demo.votingapp.pool.gateway.db.repository.PoolOptionRepository;
import com.grcp.demo.votingapp.pool.gateway.db.repository.PoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PoolGatewayAdapter implements PoolGateway {

    private final PoolRepository poolRepository;

    private final PoolOptionRepository poolOptionRepository;

    @Override
    public Pool savePool(Pool pool) {
        PoolEntity poolEntity = toEntity(pool);
        PoolEntity savedPoolEntity = poolRepository.save(poolEntity);
        return toDomain(pool, savedPoolEntity);
    }

    @Override
    public Optional<Pool> findPoolById(PoolId id) {
        return poolRepository.findById(id.value())
                .map(PoolGatewayAdapter::toDomain);
    }

    @Override
    public List<PoolOption> findPoolOptionsByPoolId(PoolId id) {
        return poolOptionRepository.findByPoolId(id.value()).stream()
                .map(PoolGatewayAdapter::toDomain)
                .toList();
    }

    @Override
    public void savePoolOptions(List<PoolOption> options) {
        List<PoolOptionEntity> poolOptionsEntity = toEntities(options);
        poolOptionRepository.saveAll(poolOptionsEntity);
    }

    private static Pool toDomain(PoolEntity poolEntity) {
        return new Pool(
                new PoolId(poolEntity.getId()),
                poolEntity.getDescription(),
                poolEntity.getExpiredAt(),
                null);
    }

    private static Pool toDomain(Pool pool, PoolEntity savedPoolEntity) {
        return new Pool(
                new PoolId(savedPoolEntity.getId()),
                savedPoolEntity.getDescription(),
                savedPoolEntity.getExpiredAt(),
                pool.options());
    }

    private static PoolEntity toEntity(Pool pool) {
        return PoolEntity.builder()
                .id(pool.id().value())
                .description(pool.description())
                .expiredAt(pool.expiredAt())
                .build();
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
