package com.shopee.demo.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.context.UnderwritingFlow;
import com.shopee.demo.domain.UnderwritingRequest;
import com.shopee.demo.factory.FlowMachineFactory;
import com.shopee.demo.service.DistributeLockService;
import com.shopee.demo.service.UnderwritingContextService;
import com.shopee.demo.service.UnderwritingFlowService;

import org.springframework.stereotype.Service;

@Service
public class UnderwritingFlowServiceImpl implements UnderwritingFlowService {

    @Resource
    private UnderwritingContextService underwritingContextService;

    @Resource
    private DistributeLockService distributeLockService;

    @Resource
    private FlowMachineFactory flowFactory;

    @Override
    public long createUnderwritingTask(UnderwritingRequest underwritingDO) {
        UnderwritingFlow<?> underwritingContext = underwritingContextService.create(underwritingDO);
        return underwritingContextService.save(underwritingContext);
    }

    @Override
    public void executeUnderwritingTask(long underwritingContextId) {
        UnderwritingFlow<?> underwritingContext = underwritingContextService.load(underwritingContextId);
        String underwritingId = underwritingContext.getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            flowFactory.createFlow(underwritingContextId).execute();
        });
    }

}
