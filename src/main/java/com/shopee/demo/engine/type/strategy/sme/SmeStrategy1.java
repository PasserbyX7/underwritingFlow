package com.shopee.demo.engine.type.strategy.sme;

import java.util.List;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.service.intergration.DataIntegration;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.strategy.AbstractStrategy;
import com.shopee.demo.engine.type.strategy.StrategyResult;

import org.springframework.stereotype.Component;

@Component
public class SmeStrategy1 extends AbstractStrategy<SmeUnderwritingRequest> {

    @Resource
    private DataIntegration<SmeUnderwritingRequest> creditRiskIntegration;

    @Resource
    private DataIntegration<SmeUnderwritingRequest> initiationIntegration;

    @Override
    public StrategyResult execute(StrategyContext<SmeUnderwritingRequest> strategyContext) {
        StrategyResult result = StrategyResult.pass();
        return result;
    }

    @Override
    public StrategyEnum getStrategyName() {
        return StrategyEnum.SME_STRATEGY_1;
    }

    @Override
    protected List<DataIntegration<SmeUnderwritingRequest>> getDataIntegrations() {
        return Lists.newArrayList();
    }

}
