package com.shopee.demo.engine.entity.machine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableMap;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.config.FlowMachineBuilder;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.machine.ExtendedStateEnum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static com.shopee.demo.engine.type.flow.FlowEventEnum.*;
import static com.shopee.demo.engine.type.strategy.StrategyStatusEnum.*;
import static com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingFlowExecuteServiceTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class StateMachineTest {

    @InjectMocks
    private FlowMachineBuilder flowMachineBuilder;

    @Mock
    private FlowStateMachinePersistService flowStateMachinePersistService;

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private UnderwritingFlow underwritingFlow;

    @Test
    void testInitial2Approved() throws Exception {
        // given
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(true, true, false).when(underwritingFlow).hasNextStrategy();
        doReturn(PASS, PASS, PASS).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine = mockMachine();
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(100)
                .stateMachine(machine)
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(4)
                .expectStateEntered(ONGOING, ONGOING, ONGOING, APPROVED)
                .expectStates(APPROVED)
                .and()
                .build()
                .test();
        verify(flowStateMachinePersistService, times(4)).persist(any());
    }

    @Test
    void testInitial2Rejected() {

    }

    @Test
    void testInitial2Cancelled() {

    }

    @Test
    void testInitial2Expired() {

    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> mockMachine()
            throws Exception {
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine = flowMachineBuilder.build();
        StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> context = new DefaultStateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum>(
                underwritingFlow.getFlowStatus(),
                null,
                null,
                new DefaultExtendedState(ImmutableMap.of(ExtendedStateEnum.UNDERWRITING_CONTEXT, underwritingFlow)),
                null,
                FlowMachineBuilder.FLOW_STATE_MACHINE_ID);
        machine.getStateMachineAccessor().doWithAllRegions(f -> f.resetStateMachineReactively(context).block());
        machine.startReactively().block();
        return machine;
    }

}
