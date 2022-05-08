package com.shopee.demo.engine.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.FlowStateMachinePoolService;
import com.shopee.demo.engine.service.UnderwritingFlowExecuteService;
import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowExecuteServiceImpl implements UnderwritingFlowExecuteService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private FlowStateMachinePoolService flowStateMachinePoolService;

    @Resource
    private DistributeLockService distributeLockService;

    @Override
    public void executeUnderwritingFlowAsync(long underwritingFlowId) {
        UnderwritingFlow underwritingFlow = underwritingFlowRepository.find(underwritingFlowId);
        String underwritingId = underwritingFlow.getUnderwritingRequest().getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            // 创建状态机
            FlowStateMachine flowStateMachine = flowStateMachinePoolService.acquire(underwritingFlowId);
            // 执行状态机
            flowStateMachine.execute();
            // 销毁状态机
            flowStateMachinePoolService.release(flowStateMachine);
            return null;
        });
    }

}
