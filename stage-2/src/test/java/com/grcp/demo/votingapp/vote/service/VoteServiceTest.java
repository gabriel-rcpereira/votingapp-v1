package com.grcp.demo.votingapp.vote.service;

import com.grcp.demo.votingapp.config.ValidationAdvice;
import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.pool.fixture.PoolFixture;
import com.grcp.demo.votingapp.pool.service.PoolService;
import com.grcp.demo.votingapp.shared.error.exception.BusinessException;
import com.grcp.demo.votingapp.vote.domain.AggregatedVotingResult;
import com.grcp.demo.votingapp.vote.domain.PoolOptionVotingResult;
import com.grcp.demo.votingapp.vote.domain.Vote;
import com.grcp.demo.votingapp.vote.domain.VotingResult;
import com.grcp.demo.votingapp.vote.fixture.VoteFixture;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.aop.framework.ProxyFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class VoteServiceTest {

    private final VoteAdapter voteAdapter = mock(VoteAdapter.class);
    private final PoolService poolService = mock(PoolService.class);
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        ProxyFactory serviceProxyFactory = new ProxyFactory(new VoteService(voteAdapter, poolService));
        serviceProxyFactory.addAdvice(new ValidationAdvice());
        voteService = (VoteService) serviceProxyFactory.getProxy();
    }

    @Nested
    class RegisterNewVoteTest {

        @Test
        public void givenValidParameters_ThenCreatesNewVote() {
            // given
            PoolId poolId = PoolId.asNew();
            PoolOptionId votedPoolOptionId = PoolOptionId.asNew();
            Vote newVote = VoteFixture.validVote(votedPoolOptionId);
            Pool mockedPool = PoolFixture.validPool(votedPoolOptionId, poolId);

            given(poolService.fetchPool(any(PoolId.class))).willReturn(mockedPool);
            doNothing().when(voteAdapter).saveVote(any(Vote.class));

            // when
            voteService.registerNewVote(poolId, newVote);

            // then
            InOrder inOrder = inOrder(poolService, voteAdapter);

            inOrder.verify(poolService, times(1)).fetchPool(eq(poolId));
            inOrder.verify(voteAdapter, times(1)).saveVote(eq(newVote));
        }

        @Test
        public void givenInvalidPoolId_ThenThrowsTheErrorException() {
            // given
            PoolOptionId votedPoolOptionId = PoolOptionId.asNew();
            Vote newVote = VoteFixture.validVote(votedPoolOptionId);

            // when
            assertThatThrownBy(() -> voteService.registerNewVote(new PoolId(null), newVote))
                    .isInstanceOf(ConstraintViolationException.class);

            // then
            verifyNoInteractions(poolService, voteAdapter);
        }

        @Test
        public void givenInvalidVote_ThenThrowsTheErrorException() {
            // when
            assertThatThrownBy(() -> voteService.registerNewVote(
                        PoolId.asNew(),
                        VoteFixture.validVote(null)))
                    .isInstanceOf(ConstraintViolationException.class);

            // then
            verifyNoInteractions(poolService, voteAdapter);
        }

        @Test
        public void givenExpiredPool_ThenThrowsException() {
            // given
            PoolId poolId = PoolId.asNew();
            PoolOptionId votedPoolOptionId = PoolOptionId.asNew();
            Vote newVote = VoteFixture.validVote(votedPoolOptionId);
            Pool invalidPool = PoolFixture.expiredPool(votedPoolOptionId, poolId);

            given(poolService.fetchPool(any(PoolId.class))).willReturn(invalidPool);

            // when
            assertThatThrownBy(() -> voteService.registerNewVote(poolId, newVote))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("002.001");

            // then
            verify(poolService, times(1)).fetchPool(eq(poolId));
            verify(voteAdapter, never()).saveVote(eq(newVote));
        }

        @Test
        public void givenPoolOptionThatDoesNotBelongToPool_ThenThrowsException() {
            // given
            PoolId validPoolId = PoolId.asNew();
            PoolOptionId invalidPoolOption = PoolOptionId.asNew();
            Vote newVote = VoteFixture.validVote(invalidPoolOption);
            Pool validPool = PoolFixture.validPool(PoolOptionId.asNew(), PoolId.asNew());

            given(poolService.fetchPool(any(PoolId.class))).willReturn(validPool);

            // when
            assertThatThrownBy(() -> voteService.registerNewVote(validPoolId, newVote))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("002.002");

            // then
            verify(poolService, times(1)).fetchPool(eq(validPoolId));
            verify(voteAdapter, never()).saveVote(eq(newVote));
        }
    }

    @Nested
    class FetchAggregatedVotingResultTest {

        @Test
        public void givenValidParameters_ThenFetchesAggregatedVotingResult() {
            // given
            PoolId poolId = PoolId.asNew();
            PoolOptionId votedPoolOptionId1 = PoolOptionId.asNew();
            PoolOptionId votedPoolOptionId2 = PoolOptionId.asNew();
            PoolOptionId votedPoolOptionId3 = PoolOptionId.asNew();
            List<PoolOptionVotingResult> poolOptionVotingResults = List.of(
                    new PoolOptionVotingResult(votedPoolOptionId1, "option 1", 10),
                    new PoolOptionVotingResult(votedPoolOptionId2, "option 2", 5),
                    new PoolOptionVotingResult(votedPoolOptionId3, "option 3", 5));

            doNothing().when(poolService).validatePoolExists(any(PoolId.class));
            given(voteAdapter.findPoolOptionVotingResultsByPoolId(any(PoolId.class))).willReturn(poolOptionVotingResults);

            // when
            AggregatedVotingResult aggregatedVotingResult = voteService.fetchAggregatedVotingResult(poolId);

            // then
            InOrder inOrder = inOrder(poolService, voteAdapter);

            inOrder.verify(poolService, times(1)).validatePoolExists(eq(poolId));
            inOrder.verify(voteAdapter, times(1)).findPoolOptionVotingResultsByPoolId(eq(poolId));

            assertThat(aggregatedVotingResult.totalPoolVotes()).isEqualTo(20);
            assertThat(aggregatedVotingResult.votingResults())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new VotingResult(votedPoolOptionId1, "option 1", 10, 50D),
                            new VotingResult(votedPoolOptionId2, "option 2", 5, 25D),
                            new VotingResult(votedPoolOptionId3, "option 3", 5, 25D)));
        }

        @Test
        public void givenPoolIdValueAsNull_ThenThrowsConstraintErrorException() {
            // given
            PoolId poolId = new PoolId(null);

            // when
            assertThatThrownBy(() -> voteService.fetchAggregatedVotingResult(poolId))
                    .isInstanceOf(ConstraintViolationException.class);

            // then
            verifyNoInteractions(poolService, voteAdapter);
        }

        @Test
        public void givenNotExistingPool_ThenThrowsException() {
            // given
            PoolId poolId = PoolId.asNew();

            doThrow(RuntimeException.class).when(poolService).validatePoolExists(eq(poolId));

            // when
            assertThatThrownBy(() -> voteService.fetchAggregatedVotingResult(poolId))
                    .isInstanceOf(RuntimeException.class);

            verifyNoInteractions(voteAdapter);
        }

        @Test
        public void givenPoolOptionVotingResultsAsEmpty_ThenReturnsEmptyAggregatedValues() {
            // given
            PoolId poolId = PoolId.asNew();

            doNothing().when(poolService).validatePoolExists(any(PoolId.class));
            given(voteAdapter.findPoolOptionVotingResultsByPoolId(eq(poolId))).willReturn(List.of());

            // when
            AggregatedVotingResult aggregatedVotingResult = voteService.fetchAggregatedVotingResult(poolId);

            // then
            assertThat(aggregatedVotingResult.totalPoolVotes()).isEqualTo(0);
            assertThat(aggregatedVotingResult.votingResults().size()).isEqualTo(0);
        }

        @Test
        public void givenPoolOptionResultsWithNoVotes_ThenReturnsAggregatedVotesAsZero() {
            // given
            PoolId poolId = PoolId.asNew();
            PoolOptionId votedPoolOptionId1 = PoolOptionId.asNew();
            PoolOptionId votedPoolOptionId2 = PoolOptionId.asNew();
            List<PoolOptionVotingResult> poolOptionVotingResults = List.of(
                    new PoolOptionVotingResult(votedPoolOptionId1, "option 1", 0),
                    new PoolOptionVotingResult(votedPoolOptionId2, "option 2", 0));

            doNothing().when(poolService).validatePoolExists(any(PoolId.class));
            given(voteAdapter.findPoolOptionVotingResultsByPoolId(eq(poolId))).willReturn(poolOptionVotingResults);

            // when
            AggregatedVotingResult aggregatedVotingResult = voteService.fetchAggregatedVotingResult(poolId);

            // then
            assertThat(aggregatedVotingResult.totalPoolVotes()).isEqualTo(0);
            assertThat(aggregatedVotingResult.votingResults())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new VotingResult(votedPoolOptionId1, "option 1", 0, 0D),
                            new VotingResult(votedPoolOptionId2, "option 2", 0, 0D)));
        }

        @Test
        public void givenOnePoolOptionResultWithNoVotes_ThenReturnsZeroToTheOption() {
            // given
            PoolId poolId = PoolId.asNew();
            PoolOptionId votedPoolOptionId1 = PoolOptionId.asNew();
            PoolOptionId votedPoolOptionId2 = PoolOptionId.asNew();
            PoolOptionId votedPoolOptionId3 = PoolOptionId.asNew();
            List<PoolOptionVotingResult> poolOptionVotingResults = List.of(
                    new PoolOptionVotingResult(votedPoolOptionId1, "option 1", 10),
                    new PoolOptionVotingResult(votedPoolOptionId2, "option 2", 0),
                    new PoolOptionVotingResult(votedPoolOptionId3, "option 3", 5));

            doNothing().when(poolService).validatePoolExists(any(PoolId.class));
            given(voteAdapter.findPoolOptionVotingResultsByPoolId(eq(poolId))).willReturn(poolOptionVotingResults);

            // when
            AggregatedVotingResult aggregatedVotingResult = voteService.fetchAggregatedVotingResult(poolId);

            // then
            assertThat(aggregatedVotingResult.totalPoolVotes()).isEqualTo(15);
            assertThat(aggregatedVotingResult.votingResults())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new VotingResult(votedPoolOptionId1, "option 1", 10, 66.67D),
                            new VotingResult(votedPoolOptionId2, "option 2", 0, 0D),
                            new VotingResult(votedPoolOptionId3, "option 3", 5, 33.33D)));
        }
    }
}