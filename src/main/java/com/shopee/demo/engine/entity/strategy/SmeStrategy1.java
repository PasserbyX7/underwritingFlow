package com.shopee.demo.engine.entity.strategy;

import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyEnum;
import com.shopee.demo.engine.type.strategy.StrategyResult;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmeStrategy1 extends Strategy<SmeUnderwritingRequest> {

    @Override
    public StrategyEnum getStrategyName() {
        return StrategyEnum.SME_STRATEGY_1;
    }

    @Override
    public StrategyResult execute(StrategyContext<SmeUnderwritingRequest> strategyContext) {
        StrategyResult result = StrategyResult.pass();
        log.info("执行策略：{} 执行结果：{}", getStrategyName(), result.getStatus());
        return result;
    }

}
