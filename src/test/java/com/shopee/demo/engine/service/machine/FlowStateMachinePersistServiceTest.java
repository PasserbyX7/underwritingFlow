package com.shopee.demo.engine.service.machine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.machine.impl.FlowStateMachinePersistServiceImpl;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccessor;
import org.springframework.statemachine.support.DefaultExtendedState;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowStateMachinePersistServiceTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowStateMachinePersistServiceTest {

    @InjectMocks
    private FlowStateMachinePersistServiceImpl flowStateMachinePersistService;

    @Mock
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Mock
    private StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine;

    @Mock
    private StateMachineAccessor<FlowStatusEnum, FlowEventEnum> stateMachineAccessor;

    @Test
    void testPersist() throws Exception {
        // given
        ExtendedState extendedState = new DefaultExtendedState();
        doReturn(extendedState)
                .when(stateMachine)
                .getExtendedState();
        // when
        flowStateMachinePersistService.persist(stateMachine);
        // then
        verify(underwritingFlowRepository, times(1)).save(any());
    }

    @Test
    void testRestore() throws Exception {
        // given
        long underwritingFlowId = 1L;
        UnderwritingFlow underwritingFlow = mockUnderwritingFlow();
        doReturn(underwritingFlow)
                .when(underwritingFlowRepository)
                .find(anyLong());
        doReturn(stateMachineAccessor)
                .when(stateMachine)
                .getStateMachineAccessor();
        // when
        flowStateMachinePersistService.restore(stateMachine, underwritingFlowId);
        // then
        verify(underwritingFlowRepository, times(1)).find(anyLong());
        verify(stateMachineAccessor, times(1)).doWithAllRegions(any());
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
