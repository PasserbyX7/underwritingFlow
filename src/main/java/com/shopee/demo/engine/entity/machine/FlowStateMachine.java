package com.shopee.demo.engine.entity.machine;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.exception.flow.FlowException;

import org.springframework.context.Lifecycle;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.util.StopWatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class FlowStateMachine {

    private static String STATE_MACHINE_EXCEPTION = "StateMachineException";

    @Getter
    private final StateMachine<FlowStatusEnum, FlowEventEnum> machine;

    public void start() {
        if (!((Lifecycle) machine).isRunning()) {
            machine.startReactively().block();
        }
    }

    /**
     *
     * @param timeout specify underwriting timeout in milliseconds
     */
    public void execute(long timeout) {
        if (((Lifecycle) machine).isRunning()) {
            long duration = doExecute(timeout);
            log.info("underwriting flow execute {} ms [{}] ", duration,
                    UnderwritingFlow.from(machine.getExtendedState()));
            if (duration > timeout) {
                throw new FlowException("flow underwriting timeout");
            }
            if (machine.hasStateMachineError()) {
                Exception e = machine.getExtendedState().get(STATE_MACHINE_EXCEPTION, Exception.class);
                throw new FlowException(e);
            }
        }
    }

    public void stop() {
        if (((Lifecycle) machine).isRunning()) {
            machine.stopReactively().block();
        }
    }

    public Long getUnderwritingFlowId() {
        return UnderwritingFlow.from(machine.getExtendedState()).getId();
    }

    /**
     *
     * @return 授信耗时（单位毫秒）
     */
    private long doExecute(long timeout) {
        StopWatch sw = new StopWatch();
        ExecuteListener listener = new ExecuteListener(machine);
        machine.addStateListener(listener);
        try {
            sw.start();
            machine.sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.START).build())).subscribe();
            listener.latch.await(timeout, TimeUnit.MILLISECONDS);
            sw.stop();
        } catch (InterruptedException e) {
        }
        return sw.getLastTaskTimeMillis();
    }

    @AllArgsConstructor
    private static class ExecuteListener
            extends StateMachineListenerAdapter<FlowStatusEnum, FlowEventEnum> {
        final CountDownLatch latch = new CountDownLatch(1);
        private final StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine;

        @Override
        public void stateEntered(State<FlowStatusEnum, FlowEventEnum> state) {
            if (state.getId().isTerminal() || state.getId() == FlowStatusEnum.PENDING) {
                stateMachine.removeStateListener(this);
                latch.countDown();
            }
        }

        @Override
        public void stateMachineError(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine, Exception exception) {
            stateMachine.removeStateListener(this);
            stateMachine.getExtendedState().getVariables().put(STATE_MACHINE_EXCEPTION, exception);
            latch.countDown();
        }

    }

}
