package com.shopee.demo.engine.domain.type.strategy;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.domain.service.strategy.Strategy;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;

public interface StrategyChain<T extends UnderwritingRequest> {

    Strategy<T> getFirstStrategy();

    Strategy<T> getStrategy(StrategyEnum strategyName);
}
