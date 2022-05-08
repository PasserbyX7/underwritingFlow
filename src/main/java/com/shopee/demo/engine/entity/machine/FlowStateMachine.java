package com.shopee.demo.engine.entity.machine;

import java.util.concurrent.CountDownLatch;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.machine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.context.Lifecycle;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor(staticName = "of")
public class FlowStateMachine {
    private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine;

    public FlowStateMachine start() {
        if (!((Lifecycle) machine).isRunning()) {
            StartListener listener = new StartListener(machine);
            machine.addStateListener(listener);
            machine.startReactively().block();
            try {
                listener.latch.await();
            } catch (InterruptedException e) {
            }
        }
        return this;
    }

    public FlowStateMachine execute() {
        if (((Lifecycle) machine).isRunning()) {
            ExecuteListener listener = new ExecuteListener(machine);
            machine.addStateListener(listener);
            machine.sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.START).build())).blockLast();
            try {
                listener.latch.await();
            } catch (InterruptedException e) {
            }
        }
        return this;
    }

    public FlowStateMachine stop() {
        if (((Lifecycle) machine).isRunning()) {
            StopListener listener = new StopListener(machine);
            machine.addStateListener(listener);
            machine.stopReactively().block();
            try {
                listener.latch.await();
            } catch (InterruptedException e) {
            }
        }
        return this;
    }

    public FlowStateMachine restore(StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> context)
            throws Exception {
        // @formatter:off
        machine.getStateMachineAccessor().doWithAllRegions(function -> function.resetStateMachineReactively(context).block());
        return this;
        // @formatter:on
    }

    public Long getUnderwritingFlowId() {
        return machine.getExtendedState()
                .get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class)
                .getId();
    }

    @AllArgsConstructor
    private static class StartListener extends StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {

        final CountDownLatch latch = new CountDownLatch(1);
        private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;

        @Override
        public void stateMachineStarted(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
            this.stateMachine.removeStateListener(this);
            latch.countDown();
        }
    }

    @AllArgsConstructor
    private static class ExecuteListener
            extends StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {
        final CountDownLatch latch = new CountDownLatch(1);
        private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;

        @Override
        public void stateEntered(State<UnderwritingFlowStatusEnum, FlowEventEnum> state) {
            // TODO 考虑PENDING态
            if (state.getId().isTerminal()) {
                this.stateMachine.removeStateListener(this);
                latch.countDown();
            }
        }

    }

    @AllArgsConstructor
    private static class StopListener extends StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {
        final CountDownLatch latch = new CountDownLatch(1);
        private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;

        @Override
        public void stateMachineStopped(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
            this.stateMachine.removeStateListener(this);
            latch.countDown();
        }
    }

}
