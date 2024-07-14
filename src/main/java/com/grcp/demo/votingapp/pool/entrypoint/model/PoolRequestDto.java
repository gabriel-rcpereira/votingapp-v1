package com.grcp.demo.votingapp.pool.entrypoint.model;

import java.time.LocalDateTime;
import java.util.List;

public record PoolRequestDto (
        String description,
        LocalDateTime expiredAt,
        List<PoolOptionRequestDto> options) {
}
