package com.shopee.demo.engine.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Callable;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.strategy.Strategy;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.impl.UnderwritingFlowExecuteServiceImpl;
import com.shopee.demo.engine.type.factory.StrategyChainFactory;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.strategy.AbstractStrategyChain;
import com.shopee.demo.engine.type.strategy.StrategyChain;
import com.shopee.demo.engine.type.strategy.sme.SmeStrategy1;
import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingFlowExecuteServiceTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingFlowExecuteServiceTest {

    @InjectMocks
    private UnderwritingFlowExecuteServiceImpl underwritingFlowExecuteService;

    @Mock
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Mock
    private DistributeLockService distributeLockService;

    @Mock
    private FlowStateMachineService flowStateMachineService;

    @BeforeAll
    static void beforeAll() {
        StrategyChain<SmeUnderwritingRequest> smeStrategyChain = mockSmeStrategyChain();
        mockStatic(StrategyChainFactory.class)
                .when(() -> StrategyChainFactory.getStrategyChain(eq(UnderwritingTypeEnum.SME)))
                .thenReturn(smeStrategyChain);
    }

    @Test
    void testExecuteUnderwritingFlowAsync() {
        // given
        long underwritingFlowId = 1L;
        UnderwritingFlow<?> underwritingFlow = mockUnderwritingFlow();
        // when
        doReturn(underwritingFlow)
                .when(underwritingFlowRepository)
                .find(anyLong());

        doAnswer(inv -> inv.<Callable<Object>>getArgument(1).call())
                .when(distributeLockService)
                .executeWithDistributeLock(anyString(), any());
        // then
        underwritingFlowExecuteService.executeUnderwritingFlowAsync(underwritingFlowId);
        verify(flowStateMachineService, times(1)).acquire(anyLong());
        verify(flowStateMachineService, times(1)).execute(any());
        verify(flowStateMachineService, times(1)).release(anyLong());
    }

    private UnderwritingFlow<?> mockUnderwritingFlow() {
        return UnderwritingFlow.of(mockSmeUnderwritingRequest());
    }

    private UnderwritingRequest mockSmeUnderwritingRequest() {
        return new UnderwritingRequest() {

            @Override
            public UnderwritingTypeEnum getUnderwritingType() {
                return UnderwritingTypeEnum.SME;
            }

            @Override
            public String getUnderwritingId() {
                return "mockedUnderwritingId";
            }

            @Override
            public Long getRequestTime() {
                return 1651922495900L;
            }

            @Override
            public Long getRequestExpireTime() {
                return 1651922495900L;
            }

        };
    }

    private static StrategyChain<SmeUnderwritingRequest> mockSmeStrategyChain() {
        return new AbstractStrategyChain<SmeUnderwritingRequest>() {

            @Override
            public Strategy<SmeUnderwritingRequest> getFirstStrategy() {
                return new SmeStrategy1();
            }

            @Override
            protected void configStrategyChain() {
            }

        };
    }
}
