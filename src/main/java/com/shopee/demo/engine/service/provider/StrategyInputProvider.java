package com.shopee.demo.engine.service.provider;

import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyInput;

public interface StrategyInputProvider<T extends UnderwritingRequest> {
    StrategyInput provide(StrategyContext<T> strategyContext);
}
