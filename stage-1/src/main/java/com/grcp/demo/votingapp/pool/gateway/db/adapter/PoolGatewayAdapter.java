package com.grcp.demo.votingapp.pool.gateway.db.adapter;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.gateway.PoolGateway;
import com.grcp.demo.votingapp.pool.gateway.db.entity.PoolEntity;
import com.grcp.demo.votingapp.pool.gateway.db.repository.PoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PoolGatewayAdapter implements PoolGateway {

    private final PoolRepository poolRepository;

    @Override
    public Pool save(Pool pool) {
        PoolEntity poolEntity = toEntity(pool);
        PoolEntity savedPoolEntity = poolRepository.save(poolEntity);
        return toDomain(pool, savedPoolEntity);
    }

    @Override
    public Optional<Pool> findById(Long id) {
        return poolRepository.findById(id)
                .map(PoolGatewayAdapter::toDomain);
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
}
