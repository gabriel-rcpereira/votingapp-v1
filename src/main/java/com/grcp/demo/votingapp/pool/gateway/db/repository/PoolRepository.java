package com.grcp.demo.votingapp.pool.gateway.db.repository;

import com.grcp.demo.votingapp.pool.gateway.db.entity.PoolEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends CrudRepository<PoolEntity, Long> {
}
