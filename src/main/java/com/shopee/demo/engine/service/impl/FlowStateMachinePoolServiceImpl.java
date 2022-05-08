package com.shopee.demo.engine.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.service.FlowStateMachinePoolService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FlowStateMachinePoolServiceImpl implements FlowStateMachinePoolService {

    @Resource
    private GenericObjectPool<FlowStateMachine> flowMachinePool;

    @Resource
    private StateMachinePersist<UnderwritingFlowStatusEnum, FlowEventEnum, Long> stateMachinePersist;

    @Override
    public FlowStateMachine acquire(long underwritingFlowId) {
        log.info("Acquiring machine with underwriting flow id[{}]", underwritingFlowId);
        try {
            FlowStateMachine machine = flowMachinePool.borrowObject();
            machine.restore(stateMachinePersist.read(underwritingFlowId));
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