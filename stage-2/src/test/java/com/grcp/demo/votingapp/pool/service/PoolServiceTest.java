package com.grcp.demo.votingapp.pool.service;

import com.grcp.demo.votingapp.config.ValidationAdvice;
import com.grcp.demo.votingapp.pool.domain.Pool;
import com.grcp.demo.votingapp.pool.domain.PoolId;
import com.grcp.demo.votingapp.pool.domain.PoolOption;
import com.grcp.demo.votingapp.pool.domain.PoolOptionId;
import com.grcp.demo.votingapp.shared.error.exception.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.aop.framework.ProxyFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PoolServiceTest {

    private final PoolAdapter poolAdapter = mock();
    private PoolService poolService;

    @BeforeAll
    void beforeAll() {
        ProxyFactory serviceProxyFactory = new ProxyFactory(new PoolService(poolAdapter));
        serviceProxyFactory.addAdvice(new ValidationAdvice());
        poolService = (PoolService) serviceProxyFactory.getProxy();
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_METHOD)
    class FetchPoolTest {
        @Test
        void shouldFetchPoolGivenValidPoolId() {
            // given
            var poolId = PoolId.asNew();
            var poolOption = new PoolOption(PoolOptionId.asNew(), "option 1", poolId);
            var mockedPool = new Pool(poolId, "mocked pool", LocalDateTime.now().plusDays(3), List.of(poolOption));

            given(poolAdapter.findPoolById(poolId)).willReturn(Optional.of(mockedPool));

            // when
            var actualPool = poolService.fetchPool(poolId);

            // then
            assertThat(actualPool).usingRecursiveComparison().isEqualTo(mockedPool);
        }

        @Test
        void shouldThrowEntityNotFoundGivenInvalidPoolId() {
            // given
            var invalidPoolId = PoolId.asNew();

            given(poolAdapter.findPoolById(invalidPoolId)).willReturn(Optional.empty());

            // when
            var actual = assertThatThrownBy(() -> poolService.fetchPool(invalidPoolId));

            // then
            actual.isInstanceOf(EntityNotFoundException.class).hasMessage("001.001");
        }

        @Test
        void shouldThrowAnExceptionGivenInvalidPoolId() {
            // given
            var invalidPoolIdAsNull = new PoolId(null);

            // when
            var actual = assertThatThrownBy(() -> poolService.fetchPool(invalidPoolIdAsNull));

            // then
            actual.isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("001.010");
        }
    }
}