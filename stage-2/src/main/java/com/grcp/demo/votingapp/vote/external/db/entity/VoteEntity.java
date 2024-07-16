package com.grcp.demo.votingapp.vote.external.db.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("VOTE")
public class VoteEntity {

    @Id
    private Long id;
    private Long poolOptionId;
    @Version
    private Integer version;
}
