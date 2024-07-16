package com.grcp.demo.votingapp.pool.external.db.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("POOL_OPTION")
@Data
public class PoolOptionEntity {

    @Id
    private Long id;
    private String description;
    private Long poolId;
    @Version
    private Integer version;
}
