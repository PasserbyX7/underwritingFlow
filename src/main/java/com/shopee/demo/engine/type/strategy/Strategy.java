package com.shopee.demo.engine.type.strategy;

import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

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
