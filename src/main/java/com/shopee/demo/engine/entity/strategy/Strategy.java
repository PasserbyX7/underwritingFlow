package com.shopee.demo.engine.entity.strategy;

import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyEnum;
import com.shopee.demo.engine.type.strategy.StrategyResult;

import lombok.Getter;
import lombok.Setter;

public abstract class Strategy<T extends UnderwritingRequest> {
    public abstract StrategyEnum getStrategyName();

    @Setter
    @Getter
    protected Strategy<T> nextStrategy;

    public abstract StrategyResult execute(StrategyContext<T> strategyContext);

    public boolean hasNextStrategy() {
        return nextStrategy != null;
    }
}
