package com.shopee.demo.engine.type.strategy;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface Strategy<T extends UnderwritingRequest> {

    StrategyEnum getStrategyName();

    StrategyResult execute(StrategyContext<T> strategyContext);

    Strategy<T> getNextStrategy();

    void setNextStrategy(Strategy<T> strategy);

    default boolean hasNextStrategy() {
        return getNextStrategy() != null;
    }

}
