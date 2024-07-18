package com.grcp.demo.votingapp.pool.gateway.db.repository;

import com.grcp.demo.votingapp.pool.gateway.db.entity.PoolOptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolOptionRepository extends CrudRepository<PoolOptionEntity, Long> {

    List<PoolOptionEntity> findByPoolId(Long poolId);
}
