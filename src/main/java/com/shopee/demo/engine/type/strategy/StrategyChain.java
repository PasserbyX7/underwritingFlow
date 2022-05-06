package com.shopee.demo.engine.type.strategy;

import com.shopee.demo.engine.entity.strategy.Strategy;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface StrategyChain<T extends UnderwritingRequest> {

    Strategy<T> getFirstStrategy();

    Strategy<T> getStrategy(StrategyEnum strategyName);
}
