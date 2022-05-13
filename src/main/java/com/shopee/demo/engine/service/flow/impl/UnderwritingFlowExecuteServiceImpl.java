package com.shopee.demo.engine.service.flow.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.config.FlowStateMachineProperties;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.flow.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.service.machine.FlowStateMachinePoolService;
import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowExecuteServiceImpl implements UnderwritingFlowExecuteService {

    @Resource
    private FlowStateMachineProperties flowStateMachineProperties;

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
            FlowStateMachine flowStateMachine = null;
            try {
                // 创建状态机
                flowStateMachine = flowStateMachinePoolService.acquire(underwritingFlowId);
                // 执行状态机
                flowStateMachine.execute(flowStateMachineProperties.getFlowTimeout());
                return null;
            } finally {
                // 销毁状态机
                if (flowStateMachine != null) {
                    flowStateMachinePoolService.release(flowStateMachine);
                }
            }
        });
    }

}
