package com.shopee.demo.engine.service.flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Callable;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.flow.impl.UnderwritingFlowExecuteServiceImpl;
import com.shopee.demo.engine.service.machine.FlowStateMachinePoolService;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    private FlowStateMachinePoolService flowStateMachinePoolService;

    @Mock
    private FlowStateMachine flowStateMachine;

    @Test
    void testExecuteUnderwritingFlowAsync() {
        // given
        long underwritingFlowId = 1L;
        UnderwritingFlow underwritingFlow = mockUnderwritingFlow();
        doReturn(underwritingFlow)
                .when(underwritingFlowRepository)
                .find(anyLong());
        doReturn(flowStateMachine)
                .when(flowStateMachinePoolService)
                .acquire(anyLong());
        doAnswer(inv -> inv.<Callable<Object>>getArgument(1).call())
                .when(distributeLockService)
                .executeWithDistributeLock(anyString(), any());
        // when
        underwritingFlowExecuteService.executeUnderwritingFlowAsync(underwritingFlowId);
        // then
        verify(flowStateMachinePoolService, times(1)).acquire(anyLong());
        verify(flowStateMachine, times(1)).execute();
        verify(flowStateMachinePoolService, times(1)).release(any());
    }

    private UnderwritingFlow mockUnderwritingFlow() {
        return UnderwritingFlow.of(new UnderwritingRequest() {

            @Override
            public UnderwritingTypeEnum getUnderwritingType() {
                return UnderwritingTypeEnum.SME;
            }

            @Override
            public String getUnderwritingId() {
                return "mockedUnderwritingId";
            }

            @Override
            public boolean isExpire() {
                return false;
            }

        });
    }

}
