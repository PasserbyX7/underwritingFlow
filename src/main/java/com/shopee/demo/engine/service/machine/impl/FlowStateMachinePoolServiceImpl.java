package com.shopee.demo.engine.service.machine.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.config.FlowMachinePoolConfig.FlowMachinePool;
import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.service.machine.FlowStateMachinePoolService;

import org.springframework.statemachine.StateMachineException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FlowStateMachinePoolServiceImpl implements FlowStateMachinePoolService {

    @Resource
    private FlowMachinePool flowMachinePool;

    @Resource
    private FlowStateMachinePersistService flowStateMachinePersistService;

    @Override
    public FlowStateMachine acquire(long underwritingFlowId) {
        log.info("Acquiring machine with underwriting flow id[{}]", underwritingFlowId);
        try {
            FlowStateMachine machine = flowMachinePool.borrowObject();
            flowStateMachinePersistService.restore(machine.getMachine(), underwritingFlowId);
            machine.start();
            return machine;
        } catch (Exception e) {
            throw new StateMachineException("Unable to acquire flow state machine", e);
        }
    }

    @Override
    public void release(FlowStateMachine flowStateMachine) {
        log.info("Releasing machine with underwriting flow id[{}]", flowStateMachine.getUnderwritingFlowId());
        try {
            flowMachinePool.returnObject(flowStateMachine);
        } catch (Exception e) {
            throw new StateMachineException("Unable to release flow state machine", e);
        }
    }

}