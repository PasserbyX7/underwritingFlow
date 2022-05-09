package com.shopee.demo.engine.service.machine;

import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachineContext;

public interface FlowStateMachinePersistService {
    /**
     * Write a flow state machine into a persistent store
     *
     * @param context the context
     * @throws Exception the exception
     */
    void write(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) throws Exception;

    /**
     * Read a flow state machine from a persistent store
     * with a underwriting flow id
     *
     * @param underwritingFlowId underwriting flow id
     * @return the state machine context
     * @throws Exception the exception
     */
    StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> read(long underwritingFlowId) throws Exception;
}
