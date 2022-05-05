package com.shopee.demo.engine.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.domain.entity.UnderwritingFlow;
import com.shopee.demo.engine.domain.entity.factory.FlowMachineFactory;
import com.shopee.demo.engine.domain.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;
import com.shopee.demo.engine.service.UnderwritingFlowService;
import com.shopee.demo.infrastructure.service.DistributeLockService;

import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowServiceImpl implements UnderwritingFlowService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private DistributeLockService distributeLockService;

    @Resource
    private FlowMachineFactory flowFactory;

    @Override
    public long createUnderwritingTask(UnderwritingRequest underwritingRequest) {
        UnderwritingFlow<?> underwritingContext = UnderwritingFlow.of(underwritingRequest);
        return underwritingFlowRepository.save(underwritingContext);
    }

    @Override
    public void executeUnderwritingTask(long underwritingFlowId) {
        //创建状态机
        //执行状态机
        //销毁状态机
        UnderwritingFlow<?> underwritingFlow = underwritingFlowRepository.load(underwritingFlowId);
        String underwritingId = underwritingFlow.getUnderwritingRequest().getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            flowFactory.createFlow(underwritingFlowId).execute();
        });
        //采用B模型
        //将underwritingFlow加载进内存
        //创建FlowStatusMachine
        //等待machine执行完毕
        //销毁FlowStatusMachine
    }

}
