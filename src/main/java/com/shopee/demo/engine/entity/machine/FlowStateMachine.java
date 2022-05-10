package com.shopee.demo.engine.entity.machine;

import java.util.concurrent.CountDownLatch;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.context.Lifecycle;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor(staticName = "of")
public class FlowStateMachine {

    @Getter
    private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine;

    public FlowStateMachine start() {
        if (!((Lifecycle) machine).isRunning()) {
            machine.startReactively().block();
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
            machine.stopReactively().block();
        }
        return this;
    }

    public Long getUnderwritingFlowId() {
        return UnderwritingFlow.from(machine.getExtendedState()).getId();
    }

    @AllArgsConstructor
    private static class ExecuteListener
            extends StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {
        final CountDownLatch latch = new CountDownLatch(1);
        private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;

        @Override
        public void stateEntered(State<UnderwritingFlowStatusEnum, FlowEventEnum> state) {
            if (state.getId().isTerminal() || state.getId() == UnderwritingFlowStatusEnum.PENDING) {
                this.stateMachine.removeStateListener(this);
                latch.countDown();
            }
        }

    }

}
