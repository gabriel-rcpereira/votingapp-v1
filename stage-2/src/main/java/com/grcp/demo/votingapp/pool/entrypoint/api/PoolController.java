package com.grcp.demo.votingapp.pool.entrypoint.api;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.entrypoint.mapper.PoolDtoMapper;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolResponseDto;
import com.grcp.demo.votingapp.pool.service.PoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class PoolController {

    private final PoolService poolService;

    @PostMapping("/api/v1/pools")
    public ResponseEntity<Void> postNewPool(@RequestBody PoolRequestDto request) {
        Pool pool = PoolDtoMapper.toDomain(request);
        poolService.createPool(pool);
        UriComponents uriComponents = UriComponentsBuilder
                .fromPath("/api/v1/pools/{id}")
                .buildAndExpand(pool.id().value());
        URI location = URI.create(uriComponents.toUriString());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/api/v1/pools/{id}")
    public ResponseEntity<PoolResponseDto> getPool(@PathVariable("id") Long id) {
        PoolId typedId = new PoolId(id);
        Pool pool = poolService.fetchPool(typedId);
        PoolResponseDto poolResponseDto = PoolDtoMapper.toResponse(pool);
        return ResponseEntity.ok(poolResponseDto);
    }
}
