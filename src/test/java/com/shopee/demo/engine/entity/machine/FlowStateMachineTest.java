package com.shopee.demo.engine.entity.machine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.Message;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.StateMachineState;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.shopee.demo.engine.constant.FlowStatusEnum.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.exception.flow.FlowException;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowStateMachineTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowStateMachineTest {

    @InjectMocks
    private FlowStateMachine flowStateMachine;

    @Mock(extraInterfaces = { Lifecycle.class })
    private StateMachine<FlowStatusEnum, FlowEventEnum> machine;

    @Mock
    private ExtendedState extendedState;

    @Test
    void testStart() {
        // given
        doReturn(false).when((Lifecycle) machine).isRunning();
        doReturn(Mono.empty()).when(machine).startReactively();
        // when
        flowStateMachine.start();
        // then
        verify(machine, times(1)).startReactively();
    }

    @Test
    void testStop() {
        // given
        doReturn(true).when((Lifecycle) machine).isRunning();
        doReturn(Mono.empty()).when(machine).stopReactively();
        // when
        flowStateMachine.stop();
        // then
        verify(machine, times(1)).stopReactively();
    }

    @Timeout(3)
    @Test
    void testExecuteSuccess() throws Exception {
        // given
        long timeout = 1000L;
        AtomicReference<StateMachineListener<FlowStatusEnum, FlowEventEnum>> listenerRef = new AtomicReference<>();
        doReturn(true).when((Lifecycle) machine).isRunning();
        doReturn(false).when(machine).hasStateMachineError();
        doReturn(extendedState).when(machine).getExtendedState();
        doReturn(mockUnderwritingFlow())
                .when(extendedState)
                .get(eq(UnderwritingFlow.EXTENDED_STATE_KEY), eq(UnderwritingFlow.class));
        doAnswer(inv -> {
            listenerRef.set(inv.<StateMachineListener<FlowStatusEnum, FlowEventEnum>>getArgument(0));
            return null;
        }).when(machine).addStateListener(any());
        doAnswer(inv -> {
            listenerRef.get().stateEntered(new StateMachineState<>(APPROVED, machine));
            return Flux.empty();
        }).when(machine).sendEvent(ArgumentMatchers.<Mono<Message<FlowEventEnum>>>any());
        // when
        flowStateMachine.execute(timeout);
        // then
        verify(machine, times(1)).sendEvent(ArgumentMatchers.<Mono<Message<FlowEventEnum>>>any());
    }

    @Timeout(3)
    @Test
    void testExecuteFailOnTimeout() throws Exception {
        // given
        long timeout = 1000L;
        AtomicReference<StateMachineListener<FlowStatusEnum, FlowEventEnum>> listenerRef = new AtomicReference<>();
        doReturn(true).when((Lifecycle) machine).isRunning();
        doReturn(false).when(machine).hasStateMachineError();
        doReturn(extendedState).when(machine).getExtendedState();
        doReturn(mockUnderwritingFlow())
                .when(extendedState)
                .get(eq(UnderwritingFlow.EXTENDED_STATE_KEY), eq(UnderwritingFlow.class));
        doAnswer(inv -> {
            listenerRef.set(inv.<StateMachineListener<FlowStatusEnum, FlowEventEnum>>getArgument(0));
            return null;
        }).when(machine).addStateListener(any());
        doAnswer(inv -> {
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(timeout * 2);
                } catch (InterruptedException e) {
                }
                listenerRef.get().stateEntered(new StateMachineState<>(APPROVED, machine));
            }).start();
            return Flux.empty();
        }).when(machine).sendEvent(ArgumentMatchers.<Mono<Message<FlowEventEnum>>>any());
        // when
        assertThrows(FlowException.class, () -> flowStateMachine.execute(timeout));
    }

    @Timeout(3)
    @Test
    void testExecuteFailOnStateMachineException() throws Exception {
        // given
        long timeout = 1000L;
        AtomicReference<StateMachineListener<FlowStatusEnum, FlowEventEnum>> listenerRef = new AtomicReference<>();
        doReturn(true).when((Lifecycle) machine).isRunning();
        doReturn(true).when(machine).hasStateMachineError();
        doReturn(extendedState).when(machine).getExtendedState();
        doReturn(mockUnderwritingFlow())
                .when(extendedState)
                .get(eq(UnderwritingFlow.EXTENDED_STATE_KEY), eq(UnderwritingFlow.class));
        doAnswer(inv -> {
            listenerRef.set(inv.<StateMachineListener<FlowStatusEnum, FlowEventEnum>>getArgument(0));
            return null;
        }).when(machine).addStateListener(any());
        doAnswer(inv -> {
            listenerRef.get().stateMachineError(machine, new IllegalStateException());
            return Flux.empty();
        }).when(machine).sendEvent(ArgumentMatchers.<Mono<Message<FlowEventEnum>>>any());
        // when
        assertThrows(FlowException.class, () -> flowStateMachine.execute(timeout));
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
