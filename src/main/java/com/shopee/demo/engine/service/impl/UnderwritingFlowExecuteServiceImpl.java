package com.shopee.demo.engine.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.FlowStateMachineService;
import com.shopee.demo.engine.service.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowExecuteServiceImpl implements UnderwritingFlowExecuteService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private DistributeLockService distributeLockService;

    @Resource
    private FlowStateMachineService flowStateMachineService;

    @Override
    public void executeUnderwritingFlowAsync(long underwritingFlowId) {
        UnderwritingFlow underwritingFlow = underwritingFlowRepository.find(underwritingFlowId);
        String underwritingId = underwritingFlow.getUnderwritingRequest().getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            // 创建状态机
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine = flowStateMachineService.acquire(underwritingFlowId);
            // 执行状态机
            flowStateMachineService.execute(stateMachine);
            // 销毁状态机
            flowStateMachineService.release(underwritingFlowId);
            return null;
        });
    }

}
