package com.grcp.demo.votingapp.pool.entrypoint.mapper;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolOptionResponseDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolResponseDto;

import java.util.List;

public class PoolDtoMapper {

    public static Pool toDomain(PoolRequestDto request) {
        PoolId newPoolId = PoolId.asNew();
        List<PoolOption> poolOptions = request.options().stream()
                .map(option -> new PoolOption(PoolOptionId.asNew(), option.description(), newPoolId))
                .toList();
        return new Pool(newPoolId, request.description(), request.expiredAt(), poolOptions);
    }

    public static PoolResponseDto toResponse(Pool pool) {
        List<PoolOptionResponseDto> optionsResponse = pool.options().stream()
                .map(option -> new PoolOptionResponseDto(option.id().value(), option.description()))
                .toList();
        return new PoolResponseDto(pool.id().value(), pool.description(), pool.expiredAt(), optionsResponse);
    }
}
