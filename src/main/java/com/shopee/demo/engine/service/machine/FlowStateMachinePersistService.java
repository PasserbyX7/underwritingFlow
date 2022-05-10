package com.shopee.demo.engine.service.machine;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;

import org.springframework.statemachine.StateMachine;

public interface FlowStateMachinePersistService {
    /**
     * Persist a state machine
     *
     * @param stateMachine the state machine
     * @throws Exception the exception in case or any persist error
     */
    void persist(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine) throws Exception;

    /**
     * Reset a state machine with a given underwriting flow id
     * Returned machine has been reseted and is ready to be used.
     *
     * @param stateMachine       the state machine
     * @param underwritingFlowId underwriting flow id
     * @return the state machine
     * @throws Exception the exception in case or any persist error
     */
    void restore(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine, long underwritingFlowId)
            throws Exception;
}
