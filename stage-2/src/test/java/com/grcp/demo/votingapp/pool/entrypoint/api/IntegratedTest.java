package com.grcp.demo.votingapp.pool.entrypoint.api;

import com.grcp.demo.votingapp.ApplicationTests;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolOptionRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolOptionResponseDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolRequestDto;
import com.grcp.demo.votingapp.pool.entrypoint.model.PoolResponseDto;
import com.grcp.demo.votingapp.shared.error.handler.model.ApplicationErrorResponse;
import com.grcp.demo.votingapp.shared.error.handler.model.DetailedErrorResponse;
import com.grcp.demo.votingapp.vote.entrypoint.model.AggregatedVotingResultResponseDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VoteRequestDto;
import com.grcp.demo.votingapp.vote.entrypoint.model.VotingResultResponseDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegratedTest extends ApplicationTests {

    private static final String POST_POOL_URL = "/api/v1/pools";
    private static final String GET_POOL_URL = "/api/v1/pools/%s";

    @DisplayName("Should create a pool successfully given valid parameters")
    @RepeatedTest(3)
    void shouldCreateSuccessfullyGivenValidParameters() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(3);
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

        // when
        PoolResponseDto actualPoolResponse = restTemplate.getForObject(
                baseUrl() + GET_POOL_URL.formatted(postId),
                PoolResponseDto.class);

        // then
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
    @ParameterizedTest
    @CsvSource({
            "9, 3, 1, 5, 33.33, 11.11, 55.56",
            "20, 10, 3, 7, 50.0, 15.0, 35.0",
            "0, 0, 0, 0, 0.0, 0.0, 0.0",
            "100000, 50000, 35000, 15000, 50.0, 35.0, 15.0"})
    void shouldCreateAndRegisterVotesSuccessfullyGivenValidParameters(
            int numberOfVotes,
            int numberOfVotesFirsOption,
            int numberOfVotesSecondOption,
            int numberOfVotesThirdOption,
            Double percentageOfVoteFirsOption,
            Double percentageOfVoteSecondOption,
            Double percentageOfVoteThirdOption) {
        // CREATE POOL
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(3);
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
        doVotes(numberOfVotesFirsOption, postId, firstPoolOptionId);

        Long secondPoolOptionId = actualPoolResponse.options().get(1).id();
        doVotes(numberOfVotesSecondOption, postId, secondPoolOptionId);

        Long thirdPoolOptionId = actualPoolResponse.options().get(2).id();
        doVotes(numberOfVotesThirdOption, postId, thirdPoolOptionId);

        // FETCH POOL RESULT
        AggregatedVotingResultResponseDto aggregatedVotingResultResponse = restTemplate.getForObject(
                baseUrl() + "/api/v1/pools/%s/votes".formatted(postId),
                AggregatedVotingResultResponseDto.class);

        assertThat(aggregatedVotingResultResponse.totalPoolVotes()).isEqualTo(numberOfVotes);
        assertThat(aggregatedVotingResultResponse.votingResults())
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(0).id(),
                                actualPoolResponse.options().get(0).description(),
                                numberOfVotesFirsOption,
                                percentageOfVoteFirsOption),
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(1).id(),
                                actualPoolResponse.options().get(1).description(),
                                numberOfVotesSecondOption,
                                percentageOfVoteSecondOption),
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(2).id(),
                                actualPoolResponse.options().get(2).description(),
                                numberOfVotesThirdOption,
                                percentageOfVoteThirdOption),
                        new VotingResultResponseDto(
                                actualPoolResponse.options().get(3).id(),
                                actualPoolResponse.options().get(3).description(),
                                0,
                                0.0));
    }

    @DisplayName("Should not create a pool given invalid parameters pool options descriptions")
    @Test
    void shouldNotCreateAPoolWithInvalidPoolOptionsDescriptions() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(3);
        PoolRequestDto poolRequestDto = new PoolRequestDto(
                "my new pool",
                expiredAt,
                List.of(
                        new PoolOptionRequestDto(""),
                        new PoolOptionRequestDto(""),
                        new PoolOptionRequestDto("option 3")));

        // when
        var responseEntity = restTemplate.postForEntity(
                baseUrl() + POST_POOL_URL,
                poolRequestDto,
                ApplicationErrorResponse.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().errors())
                .containsExactlyInAnyOrder(
                        new DetailedErrorResponse("001.002","Pool option description cannot be null"),
                        new DetailedErrorResponse("001.002","Pool option description cannot be null"));
    }

    @DisplayName("Should not create a pool given invalid parameters pool options descriptions")
    @Test
    void shouldNotVoteGivenAPoolOptionThatDoesNotBelongToPool() {
        // CREATE POOL
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(3);
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
        String poolId = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        // REGISTER VOTES
        VoteRequestDto voteOne = new VoteRequestDto(1L);
        ResponseEntity<ApplicationErrorResponse> responseEntity = restTemplate.postForEntity(
                baseUrl() + "/api/v1/pools/%s/votes".formatted(poolId),
                voteOne,
                ApplicationErrorResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().errors())
                .containsExactlyInAnyOrder(
                        new DetailedErrorResponse("002.002", String.format("Option does not belong to Pool %s", poolId)));
    }

    @SneakyThrows
    private void doVotes(int amount, String poolId, Long poolOptionId) {
        if (amount < 1) {
            return;
        }

        VoteRequestDto voteRequest = new VoteRequestDto(poolOptionId);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < amount; i++) {
            CompletableFuture.supplyAsync(() ->
                            restTemplate.postForLocation(
                                    baseUrl() + "/api/v1/pools/%s/votes".formatted(poolId),
                                    voteRequest),
                    executorService);
        }

        executorService.shutdown();

        if (executorService.awaitTermination(3, TimeUnit.MINUTES)) {
            System.out.println("Threads have terminated");
        }
    }
}