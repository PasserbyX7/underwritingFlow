package com.shopee.demo.engine.entity.machine;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;

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

    @Getter
    private final StateMachine<FlowStatusEnum, FlowEventEnum> machine;

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
                StopWatch sw = new StopWatch();
                sw.start();
                listener.latch.await(3, TimeUnit.SECONDS);
                sw.stop();

                double duration = sw.getTotalTimeSeconds();
                log.info("underwriting flow[{}] execute {} seconds",
                        UnderwritingFlow.from(machine.getExtendedState()).toString(),
                        duration);
                if (duration > 3) {
                    // 状态机超时或状态机含有异常，都向上抛出
                    // TODO 上抛异常
                }

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
            extends StateMachineListenerAdapter<FlowStatusEnum, FlowEventEnum> {
        final CountDownLatch latch = new CountDownLatch(1);
        private final StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine;

        @Override
        public void stateEntered(State<FlowStatusEnum, FlowEventEnum> state) {
            if (state.getId().isTerminal() || state.getId() == FlowStatusEnum.PENDING) {
                this.stateMachine.removeStateListener(this);
                latch.countDown();
            }
        }

        @Override
        public void stateMachineError(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine, Exception exception) {
            this.stateMachine.removeStateListener(this);
            latch.countDown();
        }

    }

}
