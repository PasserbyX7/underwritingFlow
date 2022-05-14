package com.shopee.demo.engine.service.flow.impl;

import javax.annotation.Resource;

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
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private FlowStateMachinePoolService flowStateMachinePoolService;

    @Resource
    private DistributeLockService distributeLockService;

    @Override
    public void executeUnderwritingFlowAsync(long underwritingFlowId) {
        //TODO 如果授信flow不存在则终止流程
        UnderwritingFlow underwritingFlow = underwritingFlowRepository.find(underwritingFlowId);
        String underwritingId = underwritingFlow.getUnderwritingRequest().getUnderwritingId();
        //TODO 如果加锁失败则抛出异常
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            FlowStateMachine flowStateMachine = null;
            try {
                // 创建状态机
                flowStateMachine = flowStateMachinePoolService.acquire(underwritingFlowId);
                // 执行状态机
                flowStateMachine.execute();
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
