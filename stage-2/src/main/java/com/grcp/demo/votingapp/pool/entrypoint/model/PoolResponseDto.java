package com.grcp.demo.votingapp.pool.entrypoint.model;

import java.time.LocalDateTime;
import java.util.List;

public record PoolResponseDto(
        Long id,
        String description,
        LocalDateTime expiredAt,
        List<PoolOptionResponseDto> options) {
}
