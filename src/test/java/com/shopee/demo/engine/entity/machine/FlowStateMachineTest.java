package com.shopee.demo.engine.entity.machine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import static com.shopee.demo.engine.constant.FlowStatusEnum.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.EnumSet;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.shopee.demo.engine.config.FlowMachineBuilder;
import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;

import static com.shopee.demo.engine.constant.FlowEventEnum.*;
import static com.shopee.demo.engine.constant.FlowStatusEnum.*;
import static com.shopee.demo.engine.constant.StrategyStatusEnum.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowStateMachineTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowStateMachineTest {

    @InjectMocks
    private FlowMachineBuilder flowMachineBuilder;

    @Mock
    private UnderwritingFlow underwritingFlow;

    @Mock
    private FlowStateMachinePersistService flowStateMachinePersistService;

    @Test
    @Timeout(3)
    void testNormalFlow() throws Exception {
        // given
        List<StrategyStatusEnum> expectedStrategyStatus = Lists.newArrayList(null, PASS);
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doReturn(false).when(underwritingFlow).hasNextStrategy();
        doAnswer(inv -> expectedStrategyStatus.remove(0)).when(underwritingFlow).execute();
        doAnswer(inv -> expectedStrategyStatus.get(0)).when(underwritingFlow).getStrategyResultStatus();
        FlowStateMachine flowMachine = FlowStateMachine.of(mockMachine());
        // when
        flowMachine.start().execute().stop();
        // then
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(2)).persist(any());
    }

    @Test
    @Timeout(3)
    void testPersistException() throws Exception {
        // given
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doThrow(new IllegalArgumentException("illegal argument")).when(flowStateMachinePersistService).persist(any());
        FlowStateMachine flowMachine = FlowStateMachine.of(mockMachine());
        // when
        flowMachine.start().execute().stop();
        // then
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    @Test
    @Timeout(3)
    void testExecuteException() throws Exception {
        // given
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doThrow(new IllegalArgumentException("illegal argument")).when(underwritingFlow).execute();
        FlowStateMachine flowMachine = FlowStateMachine.of(mockMachine());
        // when
        flowMachine.start().execute().stop();
        // then
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    @Test
    @Timeout(3)
    void testSetStrategyException() throws Exception {
        // given
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doThrow(new IllegalArgumentException("illegal argument")).when(underwritingFlow).setNextStrategy();
        FlowStateMachine flowMachine = FlowStateMachine.of(mockMachine());
        // when
        flowMachine.start().execute().stop();
        // then
        verify(underwritingFlow, times(2)).execute();
        verify(underwritingFlow, times(1)).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    // TODO 状态机超时处理
    @Test
    // @Timeout(3)
    void testTimeoutException() throws Exception {
        // given
        doReturn(INITIAL).when(underwritingFlow).getFlowStatus();
        doAnswer(new AnswersWithDelay(10000000, null)).when(underwritingFlow).execute();
        FlowStateMachine flowMachine = FlowStateMachine.of(mockMachine());
        // when
        flowMachine.start().execute().stop();
        // then
        verify(underwritingFlow, times(1)).execute();
        verify(underwritingFlow, never()).setNextStrategy();
        verify(flowStateMachinePersistService, times(1)).persist(any());
    }

    private StateMachine<FlowStatusEnum, FlowEventEnum> mockMachine()
            throws Exception {
        StateMachine<FlowStatusEnum, FlowEventEnum> machine = flowMachineBuilder.build();
        StateMachineContext<FlowStatusEnum, FlowEventEnum> context = new DefaultStateMachineContext<FlowStatusEnum, FlowEventEnum>(
                underwritingFlow.getFlowStatus(),
                null,
                null,
                new DefaultExtendedState(ImmutableMap.of(UnderwritingFlow.EXTENDED_STATE_KEY, underwritingFlow)),
                null,
                FlowMachineBuilder.FLOW_STATE_MACHINE_ID);

        machine.getStateMachineAccessor().doWithAllRegions(f -> f.resetStateMachineReactively(context).block());
        machine.startReactively().block();
        return machine;
    }

}
