package com.shopee.demo.engine.entity.machine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.shopee.demo.engine.config.FlowMachineBuilder;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.machine.ExtendedStateEnum;
import com.shopee.demo.engine.type.strategy.StrategyStatusEnum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
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

    @Spy
    private BeanFactory beanFactory = spy(new StaticListableBeanFactory());

    @Mock
    private UnderwritingFlow underwritingFlow;

    @Test
    void testInitial2Ongoing2Ongoing2Approved() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS, PASS);
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(true, false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(3)
                .expectStateEntered(ONGOING, ONGOING, APPROVED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(2)).execute();
        verify(underwritingFlow, times(1)).setNextStrategy();
        verify(flowStateMachinePersistService, times(3)).persist(any());
    }

    @Test
    void testInitial2Ongoing2Ongoing2Rejected() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS, REJECT);
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(true, false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(3)
                .expectStateEntered(ONGOING, ONGOING, REJECTED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(2)).execute();
        verify(underwritingFlow, times(1)).setNextStrategy();
        verify(flowStateMachinePersistService, times(3)).persist(any());
    }

    @Test
    void testInitial2Ongoing2Ongoing2Cancelled() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS, ERROR);
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(true, false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(3)
                .expectStateEntered(ONGOING, ONGOING, CANCELLED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(2)).execute();
        verify(underwritingFlow, times(1)).setNextStrategy();
        verify(flowStateMachinePersistService, times(3)).persist(any());
    }

    @Test
    void testInitial2Ongoing2Ongoing2Pending() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS, SUSPEND);
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(true, false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(3)
                .expectStateEntered(ONGOING, ONGOING, PENDING)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(2)).execute();
        verify(underwritingFlow, times(1)).setNextStrategy();
        verify(flowStateMachinePersistService, times(3)).persist(any());
    }

    @Test
    void testInitial2Ongoing2Ongoing2Expired() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS, EXPIRE);
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(true, false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(3)
                .expectStateEntered(ONGOING, ONGOING, EXPIRED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(2)).execute();
        verify(underwritingFlow, times(1)).setNextStrategy();
        verify(flowStateMachinePersistService, times(3)).persist(any());
    }

    @Test
    void testPending2Ongoing2Pending() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, SUSPEND);
        doReturn(PENDING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(PENDING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(2)
                .expectStateEntered(ONGOING, PENDING)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(2)).persist(any());
    }

    @Test
    void testPending2Ongoing2Approved() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS);
        doReturn(PENDING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(PENDING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(2)
                .expectStateEntered(ONGOING, APPROVED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(2)).persist(any());
    }

    @Test
    void testPending2Ongoing2Rejected() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, REJECT);
        doReturn(PENDING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(PENDING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(2)
                .expectStateEntered(ONGOING, REJECTED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(2)).persist(any());
    }

    @Test
    void testPending2Ongoing2Expired() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, EXPIRE);
        doReturn(PENDING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(PENDING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(2)
                .expectStateEntered(ONGOING, EXPIRED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(2)).persist(any());
    }

    @Test
    void testPending2Ongoing2Cancelled() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, ERROR);
        doReturn(PENDING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(PENDING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(2)
                .expectStateEntered(ONGOING, CANCELLED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(2)).persist(any());
    }

    @Test
    void testOngoing2Pending() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, SUSPEND);
        doReturn(ONGOING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(ONGOING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(1)
                .expectStateEntered(PENDING)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    @Test
    void testOngoing2Approved() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS);
        doReturn(ONGOING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(ONGOING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(1)
                .expectStateEntered(APPROVED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    @Test
    void testOngoing2Rejected() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, REJECT);
        doReturn(ONGOING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(ONGOING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(1)
                .expectStateEntered(REJECTED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    @Test
    void testOngoing2Expired() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, EXPIRE);
        doReturn(ONGOING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(ONGOING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(1)
                .expectStateEntered(EXPIRED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    @Test
    void testOngoing2Cancelled() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, ERROR);
        doReturn(ONGOING).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .defaultAwaitTime(3)
                .stateMachine(mockMachine())
                .step()
                .expectStates(ONGOING)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(1)
                .expectStateEntered(CANCELLED)
                .and()
                .build()
                .test();
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
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
