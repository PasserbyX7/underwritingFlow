package com.shopee.demo.app.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.app.service.UnderwritingService;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.service.flow.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class UnderwritingServiceImpl implements UnderwritingService {

    @Resource
    private UnderwritingRequestRepository requestRepository;

    @Resource
    private UnderwritingFlowRepository flowRepository;

    @Resource
    private UnderwritingFlowExecuteService flowExecuteService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public void executeUnderwriting(UnderwritingRequest underwritingRequest) {
        // TODO 查询授信请求是否存在，如果已存在则终止流程抛出异常
        long underwritingFlowId = transactionTemplate.execute(e -> {
            requestRepository.save(underwritingRequest);
            long flowId = flowRepository.save(UnderwritingFlow.of(underwritingRequest));
            return flowId;
        });
        flowExecuteService.executeUnderwritingFlowAsync(underwritingFlowId);
    }

    @Override
    public void maintainUnderwritingFlow() {
        // TODO Auto-generated method stub

    }

}
