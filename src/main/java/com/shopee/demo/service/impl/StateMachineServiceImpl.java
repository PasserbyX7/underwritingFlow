package com.shopee.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.Lifecycle;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StateMachineServiceImpl  implements StateMachineService<UnderwritingFlowStatusEnum, FlowEventEnum>, DisposableBean{

    @Resource
	private StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineFactory;

    @Resource
	private StateMachinePersist<UnderwritingFlowStatusEnum, FlowEventEnum, Long> stateMachinePersist;

	private final Map<String, StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum>> machines = new HashMap<>();

	@Override
	public final void destroy() throws Exception {
        log.info("Entering stop sequence, stopping all managed machines");
		synchronized (machines) {
			List<String> machineIds = new ArrayList<>(machines.keySet());
			for (String machineId : machineIds) {
				releaseStateMachine(machineId, true);
			}
		}
	}

	@Override
	public StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> acquireStateMachine(String machineId) {
		return acquireStateMachine(machineId, true);
	}

	@Override
	public StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> acquireStateMachine(String machineId, boolean start) {
		log.info("Acquiring machine with id " + machineId);
		StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine;
		// naive sync to handle concurrency with release
		synchronized (machines) {
			stateMachine = machines.get(machineId);
			if (stateMachine == null) {
				log.info("Getting new machine from factory with id " + machineId);
				stateMachine = stateMachineFactory.getStateMachine(machineId);
				if (stateMachinePersist != null) {
					try {
						StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineContext = stateMachinePersist.read(Long.valueOf(machineId));
						stateMachine = restoreStateMachine(stateMachine, stateMachineContext);
					} catch (Exception e) {
						log.error("Error handling context", e);
						throw new StateMachineException("Unable to read context from store", e);
					}
				}
				machines.put(machineId, stateMachine);
			}
		}
		// handle start outside of sync as it might take some time and would block other machines acquire
		return handleStart(stateMachine, start);
	}

	@Override
	public void releaseStateMachine(String machineId) {
		log.info("Releasing machine with id " + machineId);
		synchronized (machines) {
			StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine = machines.remove(machineId);
			if (stateMachine != null) {
				log.info("Found machine with id " + machineId);
				stateMachine.stopReactively().block();
			}
		}
	}

	@Override
	public void releaseStateMachine(String machineId, boolean stop) {
		log.info("Releasing machine with id " + machineId);
		synchronized (machines) {
			StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine = machines.remove(machineId);
			if (stateMachine != null) {
				log.info("Found machine with id " + machineId);
				handleStop(stateMachine, stop);
			}
		}
	}

	/**
	 * Determines if the given machine identifier denotes a known managed state machine.
	 *
	 * @param machineId machine identifier
	 * @return true if machineId denotes a known managed state machine currently in memory
	 */
	public boolean hasStateMachine(String machineId) {
		synchronized (machines) {
			return machines.containsKey(machineId);
		}
	}

	protected StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> restoreStateMachine(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine, final StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineContext) {
		if (stateMachineContext == null) {
			return stateMachine;
		}
		stateMachine.stopReactively().block();
		stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.resetStateMachineReactively(stateMachineContext).block());
		return stateMachine;
	}

	protected StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleStart(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine, boolean start) {
		if (start) {
			if (!((Lifecycle) stateMachine).isRunning()) {
				StartListener listener = new StartListener(stateMachine);
				stateMachine.addStateListener(listener);
				stateMachine.startReactively().block();
				try {
					listener.latch.await();
				} catch (InterruptedException e) {
				}
			}
		}
		return stateMachine;
	}

	protected StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> handleStop(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine, boolean stop) {
		if (stop) {
			if (((Lifecycle) stateMachine).isRunning()) {
				StopListener listener = new StopListener(stateMachine);
				stateMachine.addStateListener(listener);
				stateMachine.stopReactively().block();
				try {
					listener.latch.await();
				} catch (InterruptedException e) {
				}
			}
		}
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
