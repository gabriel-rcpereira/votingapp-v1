package com.grcp.demo.votingapp.pool.entrypoint.api;

import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.entrypoint.mapper.PoolDtoMapper;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolResponseDto;
import com.grcp.demo.votingapp.pool.usecase.PoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Validated
@RequiredArgsConstructor
@RestController
public class PoolController {

    private final PoolService poolService;

    @PostMapping("/api/v1/pools")
    public ResponseEntity<Void> postNewPool(@RequestBody @Valid PoolRequestDto request) {
        Pool pool = PoolDtoMapper.toDomain(request);
        poolService.createPool(pool);
        UriComponents uriComponents = UriComponentsBuilder
                .fromPath("/api/v1/pools/{id}")
                .buildAndExpand(pool.id().value());
        return ResponseEntity.created(URI.create(uriComponents.toUriString())).build();
    }

    @GetMapping("/api/v1/pools/{id}")
    public ResponseEntity<PoolResponseDto> getPool(@PathVariable("id") Long id) {
        PoolId typedId = new PoolId(id);
        Pool pool = poolService.fetchPool(typedId);
        return ResponseEntity.ok(PoolDtoMapper.toResponse(pool));
    }
}
