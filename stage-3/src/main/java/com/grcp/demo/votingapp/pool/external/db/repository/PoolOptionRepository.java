package com.grcp.demo.votingapp.pool.external.db.repository;

import com.grcp.demo.votingapp.pool.external.db.entity.PoolOptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolOptionRepository extends CrudRepository<PoolOptionEntity, Long> {

    List<PoolOptionEntity> findByPoolId(Long poolId);
}
