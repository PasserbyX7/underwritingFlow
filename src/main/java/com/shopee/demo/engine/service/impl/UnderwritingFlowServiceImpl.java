package com.shopee.demo.engine.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.UnderwritingFlowService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowServiceImpl implements UnderwritingFlowService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private DistributeLockService distributeLockService;

    @Resource
    private FlowStateMachineService flowStateMachineService;

    @Override
    public long createUnderwritingTask(UnderwritingRequest underwritingRequest) {
        UnderwritingFlow<?> underwritingFlow = UnderwritingFlow.of(underwritingRequest);
        return underwritingFlowRepository.save(underwritingFlow);
    }

    @Override
    public void executeUnderwritingTaskAsync(long underwritingFlowId) {
        UnderwritingFlow<?> underwritingFlow = underwritingFlowRepository.load(underwritingFlowId);
        String underwritingId = underwritingFlow.getUnderwritingRequest().getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            // 创建状态机
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine = flowStateMachineService.acquireStateMachine(underwritingFlowId);
            // 执行状态机
            flowStateMachineService.execute(stateMachine);
            // 销毁状态机
            flowStateMachineService.releaseStateMachine(underwritingFlowId);
        });
    }

}
