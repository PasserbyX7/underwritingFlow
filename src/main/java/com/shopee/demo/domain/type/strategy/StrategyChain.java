package com.shopee.demo.domain.type.strategy;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.strategy.Strategy;

public interface StrategyChain<T extends UnderwritingRequest> {

    Strategy<T> getFirstStrategy();

    Strategy<T> getStrategy(StrategyEnum strategyName);
}
