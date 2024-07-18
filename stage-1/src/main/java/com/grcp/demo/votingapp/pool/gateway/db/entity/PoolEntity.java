package com.grcp.demo.votingapp.pool.gateway.db.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@Table("POOL")
@Data
public class PoolEntity {

    @Id
    private Long id;
    private String description;
    private LocalDateTime expiredAt;
    @Version
    private Integer version;
}
