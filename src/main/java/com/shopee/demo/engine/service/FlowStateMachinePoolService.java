package com.shopee.demo.engine.service;

import com.shopee.demo.engine.entity.machine.FlowStateMachine;

public interface FlowStateMachinePoolService {
    /**
     * Acquires the state machine. Machine from this method
     * is returned started.
     *
     * @param underwritingFlowId underwritingFlowId
     * @return the state machine
     */
    FlowStateMachine acquire(long underwritingFlowId);

    /**
     * Release the state machine. Machine should be stopped.
     *
     * @param flowStateMachine the state machine
     */
    void release(FlowStateMachine flowStateMachine);
}
