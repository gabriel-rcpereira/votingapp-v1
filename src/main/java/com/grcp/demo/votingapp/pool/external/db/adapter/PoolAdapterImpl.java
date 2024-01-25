package com.grcp.demo.votingapp.pool.external.db.adapter;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.pool.service.PoolAdapter;
import com.grcp.demo.votingapp.pool.external.db.entity.PoolEntity;
import com.grcp.demo.votingapp.pool.external.db.entity.PoolOptionEntity;
import com.grcp.demo.votingapp.pool.external.db.repository.PoolOptionRepository;
import com.grcp.demo.votingapp.pool.external.db.repository.PoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PoolAdapterImpl implements PoolAdapter {

    private final PoolRepository poolRepository;

    private final PoolOptionRepository poolOptionRepository;

    @Override
    public void savePool(Pool pool) {
        PoolEntity poolEntity = toEntity(pool);
        poolRepository.save(poolEntity);
        savePoolOptions(pool.options());
    }

    @Override
    public Optional<Pool> findPoolById(PoolId id) {
        return poolRepository.findById(id.value())
                .map(PoolAdapterImpl::toDomain)
                .map(it -> {
                    List<PoolOption> poolOptions = findPoolOptionsByPoolId(id);
                    return toDomain(it, poolOptions);
                });
    }

    private List<PoolOption> findPoolOptionsByPoolId(PoolId id) {
        return poolOptionRepository.findByPoolId(id.value()).stream()
                .map(PoolAdapterImpl::toDomain)
                .toList();
    }

    private void savePoolOptions(List<PoolOption> options) {
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

    private static Pool toDomain(Pool pool, List<PoolOption> poolOptions) {
        return new Pool(
                pool.id(),
                pool.description(),
                pool.expiredAt(),
                poolOptions);
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
