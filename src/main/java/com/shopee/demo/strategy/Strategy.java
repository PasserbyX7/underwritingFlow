package com.shopee.demo.strategy;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.constant.StrategyStatusEnum;
import com.shopee.demo.context.StrategyContext;
import com.shopee.demo.domain.UnderwritingRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Strategy<T extends UnderwritingRequest> {
    public abstract StrategyEnum getStrategyName();

    @Setter
    @Getter
    protected Strategy<T> nextStrategy;

    public StrategyContext<T> execute(StrategyContext<T> strategyContext) {
        StrategyResult result = new StrategyResult();
        result.setStatus(StrategyStatusEnum.PASS);
        strategyContext.setStrategyResult(result);
        // result.setStatus(StrategyStatusEnum.REJECT);
        log.info("执行策略：{} 执行结果：{}", getStrategyName(), result.getStatus());
        return strategyContext;
    }

}
