package com.shopee.demo.engine.machine.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.shopee.demo.engine.machine.constant.MachineId;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.Lifecycle;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FlowStateMachineServiceImpl implements FlowStateMachineService, DisposableBean {

    @Resource
    private StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineFactory;

    @Resource
    private StateMachinePersist<UnderwritingFlowStatusEnum, FlowEventEnum, Long> stateMachinePersist;

    private final Map<Long, StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum>> machines = new HashMap<>();

    @Override
    public StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> acquireStateMachine(long underwritingFlowId) {
        log.info("Acquiring machine with id " + underwritingFlowId);
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;
        // naive sync to handle concurrency with release
        synchronized (machines) {
            stateMachine = machines.get(underwritingFlowId);
            if (stateMachine == null) {
                log.info("Getting new machine from factory with id " + underwritingFlowId);
                stateMachine = stateMachineFactory.getStateMachine(MachineId.UNDERWRITING_FLOW_ID);
                try {
                    StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineContext = stateMachinePersist
                            .read(underwritingFlowId);
                    stateMachine = restoreStateMachine(stateMachine, stateMachineContext);
                } catch (Exception e) {
                    log.error("Error handling context", e);
                    throw new StateMachineException("Unable to read context from store", e);
                }
                machines.put(underwritingFlowId, stateMachine);
            }
        }
        // handle start outside of sync as it might take some time and would block other
        // machines acquire
        return handleStart(stateMachine);
    }

    @Override
    public void releaseStateMachine(long underwritingFlowId) {
        log.info("Releasing machine with id " + underwritingFlowId);
        synchronized (machines) {
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine = machines.remove(underwritingFlowId);
            if (stateMachine != null) {
                log.info("Found machine with id " + underwritingFlowId);
                handleStop(stateMachine);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("Entering stop sequence, stopping all managed machines");
        synchronized (machines) {
            List<Long> underwritingFlowIds = new ArrayList<>(machines.keySet());
            for (Long underwritingFlowId : underwritingFlowIds) {
                releaseStateMachine(underwritingFlowId);
            }
        }
    }

    @Override
    public void execute(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine) {
        handleExecute(machine);
    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleStart(
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
        if (!((Lifecycle) stateMachine).isRunning()) {
            StartListener listener = new StartListener(stateMachine);
            stateMachine.addStateListener(listener);
            stateMachine.startReactively().block();
            try {
                listener.latch.await();
            } catch (InterruptedException e) {
            }
        }
        return stateMachine;
    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleExecute(
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
        if (((Lifecycle) stateMachine).isRunning()) {
            ExecuteListener listener = new ExecuteListener(stateMachine);
            stateMachine.addStateListener(listener);
            stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.START).build())).blockLast();
            try {
                listener.latch.await();
            } catch (InterruptedException e) {
            }
        }
        return stateMachine;
    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleStop(
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
        if (((Lifecycle) stateMachine).isRunning()) {
            StopListener listener = new StopListener(stateMachine);
            stateMachine.addStateListener(listener);
            stateMachine.stopReactively().block();
            try {
                listener.latch.await();
            } catch (InterruptedException e) {
            }
        }
        return stateMachine;
    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> restoreStateMachine(
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine,
            final StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineContext) {
        if (stateMachineContext == null) {
            return stateMachine;
        }
        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(function -> function.resetStateMachineReactively(stateMachineContext).block());
        return stateMachine;
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
