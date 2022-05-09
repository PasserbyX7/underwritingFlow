package com.shopee.demo.engine.service.machine.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.service.machine.FlowStateMachinePoolService;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.statemachine.StateMachineException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FlowStateMachinePoolServiceImpl implements FlowStateMachinePoolService {

    @Resource
    private GenericObjectPool<FlowStateMachine> flowMachinePool;

    @Resource
    private FlowStateMachinePersistService flowStateMachinePersistService;

    @Override
    public FlowStateMachine acquire(long underwritingFlowId) {
        log.info("Acquiring machine with underwriting flow id[{}]", underwritingFlowId);
        try {
            FlowStateMachine machine = flowMachinePool.borrowObject();
            machine.restore(flowStateMachinePersistService.read(underwritingFlowId));
            return machine.start();
        } catch (Exception e) {
            throw new StateMachineException("Unable to acquire flow state machine", e);
        }
    }

    @Override
    public void release(FlowStateMachine flowStateMachine) {
        log.info("Releasing machine with underwriting flow id[{}]" + flowStateMachine.getUnderwritingFlowId());
        try {
            flowMachinePool.returnObject(flowStateMachine);
        } catch (Exception e) {
            throw new StateMachineException("Unable to release flow state machine", e);
        }
    }

}