package com.shopee.demo.engine.domain.entity.factory;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.entity.FlowMachine;

import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;

@Component
public class FlowMachineFactory {

    @Resource
    private StateMachineService<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineService;

    public FlowMachine createFlow(long underwritingContextId){
        return FlowMachine.of(stateMachineService.acquireStateMachine(String.valueOf(underwritingContextId)));
    }

}
