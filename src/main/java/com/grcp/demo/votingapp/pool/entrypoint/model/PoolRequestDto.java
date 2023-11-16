package com.grcp.demo.votingapp.pool.entrypoint.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record PoolRequestDto (
        String description,
        LocalDateTime expiredAt,
        List<PoolOptionRequestDto> options) {
}
