package com.grcp.demo.votingapp.vote.external.db.repository;

import com.grcp.demo.votingapp.vote.external.db.entity.VoteEntity;
import com.grcp.demo.votingapp.vote.external.db.view.VotingResultView;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Long> {

    @Query("""
        SELECT po.id as pool_option_id, po.description, count(v.pool_option_id) AS total_votes
        FROM pool_option po
            INNER JOIN pool p
                ON p.id = po.pool_id
                AND p.id = :poolId
            LEFT JOIN vote v
                ON v.pool_option_id = po.id
        GROUP BY
            po.id, po.description
        """)
    List<VotingResultView> findByPoolId(@Param("poolId") Long poolId);
}