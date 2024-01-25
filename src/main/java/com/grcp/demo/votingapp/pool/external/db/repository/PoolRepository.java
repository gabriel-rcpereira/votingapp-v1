package com.grcp.demo.votingapp.pool.external.db.repository;

import com.grcp.demo.votingapp.pool.external.db.entity.PoolEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends CrudRepository<PoolEntity, Long> {
}
