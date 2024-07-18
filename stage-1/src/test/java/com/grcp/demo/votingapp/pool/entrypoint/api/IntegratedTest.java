package com.grcp.demo.votingapp.pool.entrypoint.api;

import com.grcp.demo.votingapp.ApplicationTests;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolOptionRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolOptionResponseDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolResponseDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.AggregatedVotingResultResponseDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VoteRequestDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VotingResultResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegratedTest extends ApplicationTests {

    private static final String POST_POOL_URL = "/api/v1/pools";
    private static final String GET_POOL_URL = "/api/v1/pools/%s";

    @DisplayName("Should create a pool successfully given valid parameters")
    @RepeatedTest(3)
    void shouldCreateSuccessfullyGivenValidParameters() {
        LocalDateTime expiredAt = LocalDateTime.now().plus(3, ChronoUnit.DAYS);
        PoolRequestDto poolRequestDto = new PoolRequestDto(
                "my new pool",
                expiredAt,
                List.of(
                        new PoolOptionRequestDto("option 1"),
                        new PoolOptionRequestDto("option 2"),
                        new PoolOptionRequestDto("option 3")));
        URI createdPoolLocation = restTemplate.postForLocation(
                baseUrl() + POST_POOL_URL,
                poolRequestDto);
        Assertions.assertNotNull(createdPoolLocation.getPath());

        String urlPath = createdPoolLocation.getPath();
        String postId = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        PoolResponseDto expectedPoolResponse = new PoolResponseDto(
                Long.valueOf(postId),
                poolRequestDto.description(),
                expiredAt,
                List.of(
                        new PoolOptionResponseDto(1L, "option 1"),
                        new PoolOptionResponseDto(1L, "option 2"),
                        new PoolOptionResponseDto(1L, "option 3")));

        PoolResponseDto actualPoolResponse = restTemplate.getForObject(
                baseUrl() + GET_POOL_URL.formatted(postId),
                PoolResponseDto.class);
        Assertions.assertNotNull(actualPoolResponse);

        assertThat(actualPoolResponse)
                .usingRecursiveComparison()
                .ignoringFields("expiredAt", "options")
                .isEqualTo(expectedPoolResponse);

        assertThat(actualPoolResponse.options())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedPoolResponse.options());
    }

    @DisplayName("Should create a new pool and register its votes")
    @RepeatedTest(2)
    void shouldCreateAndRegisterVotesSuccessfullyGivenValidParameters() {
        // CREATE POOL
        LocalDateTime expiredAt = LocalDateTime.now().plus(3, ChronoUnit.DAYS);
        PoolRequestDto poolRequestDto = new PoolRequestDto(
                "my new pool",
                expiredAt,
                List.of(
                        new PoolOptionRequestDto("option 1"),
                        new PoolOptionRequestDto("option 2"),
                        new PoolOptionRequestDto("option 3"),
                        new PoolOptionRequestDto("option 4")));
        URI createdPoolLocation = restTemplate.postForLocation(
                baseUrl() + POST_POOL_URL,
                poolRequestDto);
        String urlPath = createdPoolLocation.getPath();
        String postId = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        // FETCH THE CREATED POOL
        PoolResponseDto actualPoolResponse = restTemplate.getForObject(
                baseUrl() + GET_POOL_URL.formatted(postId),
                PoolResponseDto.class);
        Assertions.assertNotNull(actualPoolResponse);

        // REGISTER VOTES
        Long firstPoolOptionId = actualPoolResponse.options().get(0).id();
        registerAmountOfVotes(3, postId, firstPoolOptionId);

        Long secondPoolOptionId = actualPoolResponse.options().get(1).id();
        registerAmountOfVotes(1, postId, secondPoolOptionId);

        Long thirdPoolOptionId = actualPoolResponse.options().get(2).id();
        registerAmountOfVotes(5, postId, thirdPoolOptionId);

        // FETCH POOL RESULT
        AggregatedVotingResultResponseDto aggregatedVotingResultResponse = restTemplate.getForObject(
                baseUrl() + "/api/v1/pools/%s/votes".formatted(postId),
                AggregatedVotingResultResponseDto.class);

        assertThat(aggregatedVotingResultResponse.totalPoolVotes()).isEqualTo(9);
        assertThat(aggregatedVotingResultResponse.votingResults())
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(0).id(),
                                actualPoolResponse.options().get(0).description(),
                                3,
                                33.33),
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(1).id(),
                                actualPoolResponse.options().get(1).description(),
                                1,
                                11.11),
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(2).id(),
                                actualPoolResponse.options().get(2).description(),
                                5,
                                55.56),
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(3).id(),
                                actualPoolResponse.options().get(3).description(),
                                0,
                                0.0));
    }

    private void registerAmountOfVotes(int amount, String postId, Long poolOptionId) {
        VoteRequestDto voteOne = new VoteRequestDto(poolOptionId);
        for (int i = 0; i < amount; i++) {
            restTemplate.postForLocation(
                    baseUrl() + "/api/v1/pools/%s/votes".formatted(postId),
                    voteOne);
        }
    }
}