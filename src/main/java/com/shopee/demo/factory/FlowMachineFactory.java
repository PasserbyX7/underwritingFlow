package com.shopee.demo.factory;

import javax.annotation.Resource;

import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.flow.FlowMachine;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

@Component
public class FlowMachineFactory {

    @Resource
    private StateMachinePersister<UnderwritingFlowStatusEnum, FlowEventEnum, Long> persister;

    @Resource
    private StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineFactory;

    public FlowMachine createFlow(long underwritingContextId){
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m = stateMachineFactory.getStateMachine("UnderwritingFlowMachine");
        try {
            persister.restore(m, underwritingContextId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FlowMachine.of(m);
    }

}
