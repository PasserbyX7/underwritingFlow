package com.shopee.demo.strategy;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.entity.StrategyContext;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.domain.type.strategy.StrategyResult;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Strategy<T extends UnderwritingRequest> {
    public abstract StrategyEnum getStrategyName();

    @Setter
    @Getter
    protected Strategy<T> nextStrategy;

    public StrategyResult execute(StrategyContext<T> strategyContext) {
        StrategyResult result = StrategyResult.pass();
        log.info("执行策略：{} 执行结果：{}", getStrategyName(), result.getStatus());
        return result;
    }

}
