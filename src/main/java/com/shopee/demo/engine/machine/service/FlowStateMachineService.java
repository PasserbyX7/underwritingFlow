package com.shopee.demo.engine.machine.service;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;

import org.springframework.statemachine.StateMachine;

public interface FlowStateMachineService {
    /**
     * Acquires the state machine. Machine from this method
     * is returned started.
     *
     * @param underwritingFlowId underwritingFlowId
     * @return the state machine
     */
    StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> acquireStateMachine(long underwritingFlowId);

    /**
     * Release the state machine. Machine should be stopped.
     *
     * @param underwritingFlowId underwritingFlowId
     */
    void releaseStateMachine(long underwritingFlowId);
}
