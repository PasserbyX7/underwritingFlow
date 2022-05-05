package com.shopee.demo.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.domain.entity.UnderwritingFlow;
import com.shopee.demo.domain.repository.UnderwritingFlowRepository;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.factory.FlowMachineFactory;
import com.shopee.demo.service.DistributeLockService;
import com.shopee.demo.service.UnderwritingFlowService;

import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowServiceImpl implements UnderwritingFlowService {

    @Resource
    private UnderwritingFlowRepository underwritingContextService;

    @Resource
    private DistributeLockService distributeLockService;

    @Resource
    private FlowMachineFactory flowFactory;

    @Override
    public long createUnderwritingTask(UnderwritingRequest underwritingRequest) {
        UnderwritingFlow<?> underwritingContext = UnderwritingFlow.of(underwritingRequest);
        return underwritingContextService.save(underwritingContext);
    }

    @Override
    public void executeUnderwritingTask(long underwritingContextId) {
        UnderwritingFlow<?> underwritingContext = underwritingContextService.load(underwritingContextId);
        String underwritingId = underwritingContext.getUnderwritingRequest().getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            flowFactory.createFlow(underwritingContextId).execute();
        });
    }

}
