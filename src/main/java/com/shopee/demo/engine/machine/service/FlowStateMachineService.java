package com.shopee.demo.engine.machine.service;

import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

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
     * Execute the state machine.
     * @param stateMachine flow state machine
     */
    void execute(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum>stateMachine);
    /**
     * Release the state machine. Machine should be stopped.
     *
     * @param underwritingFlowId underwritingFlowId
     */
    void releaseStateMachine(long underwritingFlowId);
}
