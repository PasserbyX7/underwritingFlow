package com.shopee.demo.engine.machine.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.Lifecycle;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

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
                stateMachine = stateMachineFactory.getStateMachine("UnderwritingFlowMachine");
                if (stateMachinePersist != null) {
                    try {
                        StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineContext = stateMachinePersist
                                .read(underwritingFlowId);
                        stateMachine = restoreStateMachine(stateMachine, stateMachineContext);
                    } catch (Exception e) {
                        log.error("Error handling context", e);
                        throw new StateMachineException("Unable to read context from store", e);
                    }
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

    protected StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleStart(
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

    protected StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleStop(
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

    protected StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> restoreStateMachine(
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

    private static class StartListener extends StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {

        final CountDownLatch latch = new CountDownLatch(1);
        final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;

        public StartListener(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
            this.stateMachine = stateMachine;
        }

        @Override
        public void stateMachineStarted(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
            this.stateMachine.removeStateListener(this);
            latch.countDown();
        }
    }

    private static class StopListener extends StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {

        final CountDownLatch latch = new CountDownLatch(1);
        final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;

        public StopListener(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
            this.stateMachine = stateMachine;
        }

        @Override
        public void stateMachineStopped(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) {
            this.stateMachine.removeStateListener(this);
            latch.countDown();
        }
    }

}
