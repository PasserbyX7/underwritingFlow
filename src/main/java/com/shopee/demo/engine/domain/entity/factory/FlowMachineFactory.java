package com.shopee.demo.engine.domain.entity.factory;

import javax.annotation.Resource;

import com.shopee.demo.engine.domain.entity.FlowMachine;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;

import org.springframework.stereotype.Component;

@Component
public class FlowMachineFactory {

    @Resource
    private FlowStateMachineService flowStateMachineService;

    public FlowMachine createFlow(long underwritingFlowId){
        return FlowMachine.of(flowStateMachineService.acquireStateMachine(underwritingFlowId));
    }

}
