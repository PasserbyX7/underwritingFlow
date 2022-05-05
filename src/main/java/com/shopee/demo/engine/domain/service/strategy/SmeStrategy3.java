package com.shopee.demo.engine.domain.service.strategy;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.domain.entity.StrategyContext;
import com.shopee.demo.engine.domain.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.domain.type.strategy.StrategyResult;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmeStrategy3 extends Strategy<SmeUnderwritingRequest> {

    @Override
    public StrategyEnum getStrategyName() {
        return StrategyEnum.SME_STRATEGY_3;
    }

    @Override
    public StrategyResult execute(StrategyContext<SmeUnderwritingRequest> strategyContext) {
        StrategyResult result = StrategyResult.pass();
        log.info("执行策略：{} 执行结果：{}", getStrategyName(), result.getStatus());
        return result;
    }

}
