package com.shopee.demo.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.context.StrategyContext;
import com.shopee.demo.context.UnderwritingFlow;
import com.shopee.demo.dao.UnderwritingContextDAO;
import com.shopee.demo.domain.UnderwritingRequest;
import com.shopee.demo.entity.UnderwritingContextDO;
import com.shopee.demo.factory.UnderwritingRequestFactory;
import com.shopee.demo.service.UnderwritingContextService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UnderwritingContextServiceImpl implements UnderwritingContextService {

    @Resource
    private UnderwritingRequestFactory requestFactory;

    @Resource
    private UnderwritingContextDAO underwritingContextDAO;

    @Override
    public long save(UnderwritingFlow<?> context) {
        log.info("保存授信上下文：当前策略[{}] 当前状态[{}]",
                context.getCurrentStrategy().getStrategyName(), context.getFlowStatus());
        UnderwritingContextDO underwritingContextDO = new UnderwritingContextDO();
        underwritingContextDAO.saveOrUpdateById(underwritingContextDO);
        underwritingContextDO.setId(context.getId());
        underwritingContextDO.setUnderwritingId(context.getUnderwritingId());
        underwritingContextDO.setRequestTime(context.getRequestTime());
        underwritingContextDO.setCurrentStrategy(context.getCurrentStrategy().getStrategyName());
        // underwritingContextDO.setStrategyInput(context.getStrategyInput());
        // underwritingContextDO.setStrategyOutput(context.getStrategyOutput());
        underwritingContextDO.setStatus(context.getFlowStatus());
        underwritingContextDO.setType(context.getUnderwritingType());
        underwritingContextDAO.saveOrUpdateById(underwritingContextDO);
        return underwritingContextDO.getId();
    }

    @Override
    public UnderwritingFlow<?> load(long underwritingContextId) {
        UnderwritingContextDO underwritingContextDO = underwritingContextDAO.selectByPrimaryKey(underwritingContextId);
        UnderwritingRequest req = requestFactory.create(underwritingContextDO.getType(),
                underwritingContextDO.getUnderwritingId());
        UnderwritingFlow<UnderwritingRequest> underwritingContext = new UnderwritingFlow<>(
                underwritingContextDO.getId(), req);
        underwritingContext.setFlowStatus(underwritingContextDO.getStatus());
        StrategyContext<UnderwritingRequest> strategyContext = StrategyContext.of(req);
        strategyContext.setCurrentStrategy(underwritingContextDO.getCurrentStrategy());
        strategyContext.setStrategyResult(null);// TODO
        strategyContext.setStrategyInput(underwritingContextDO.getStrategyInput());
        strategyContext.setStrategyOutput(underwritingContextDO.getStrategyOutput());
        underwritingContext.setStrategyContext(strategyContext);
        log.info("加载授信上下文：当前策略[{}] 当前状态[{}]",
                underwritingContext.getCurrentStrategy().getStrategyName(),
                underwritingContext.getFlowStatus());
        return underwritingContext;
    }

    @Override
    public UnderwritingFlow<?> create(UnderwritingRequest underwritingDO) {
        return new UnderwritingFlow<>(null, underwritingDO);
    }

}
