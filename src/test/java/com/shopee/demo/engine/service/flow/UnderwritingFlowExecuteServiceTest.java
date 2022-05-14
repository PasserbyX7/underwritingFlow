package com.shopee.demo.engine.service.flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shopee.demo.engine.config.FlowStateMachineProperties;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.flow.impl.UnderwritingFlowExecuteServiceImpl;
import com.shopee.demo.engine.service.machine.FlowStateMachinePoolService;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

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
    private FlowStateMachinePoolService flowStateMachinePoolService;

    @Mock
    private FlowStateMachine flowStateMachine;

    @Mock
    private FlowStateMachineProperties flowStateMachineProperties;

    @Test
    void testExecuteUnderwritingFlowAsync() {
        // given
        long underwritingFlowId = 1L;
        UnderwritingFlow underwritingFlow = mockUnderwritingFlow();
        doReturn(1000L)
            .when(flowStateMachineProperties)
            .getFlowTimeout();
        doReturn(underwritingFlow)
                .when(underwritingFlowRepository)
                .find(anyLong());
        doReturn(flowStateMachine)
                .when(flowStateMachinePoolService)
                .acquire(anyLong());
        // when
        underwritingFlowExecuteService.executeUnderwritingFlowAsync(underwritingFlowId);
        // then
        verify(flowStateMachinePoolService, times(1)).acquire(anyLong());
        verify(flowStateMachine, times(1)).execute(anyLong());
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
