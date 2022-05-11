package com.shopee.demo.engine.type.strategy.sme;

import java.util.List;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.service.intergration.DataIntegration;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.strategy.Strategy;
import com.shopee.demo.engine.type.strategy.StrategyResult;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmeStrategy2 extends Strategy<SmeUnderwritingRequest> {

    @Override
    public StrategyEnum getStrategyName() {
        return StrategyEnum.SME_STRATEGY_2;
    }

    @Override
    public StrategyResult execute(StrategyContext<SmeUnderwritingRequest> strategyContext) {
        StrategyResult result = StrategyResult.pass();
        log.info("执行策略：{} 执行结果：{}", getStrategyName(), result.getStatus());
        return result;
    }

    @Override
    protected List<DataIntegration<SmeUnderwritingRequest>> getDataIntegrations() {
        // TODO Auto-generated method stub
        return null;
    }

}
