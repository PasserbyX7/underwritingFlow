package com.shopee.demo.engine.service.intergration;

import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface DataIntegration<T extends UnderwritingRequest> {
    void integration(StrategyContext<T> strategyContext, EngineInput engineInput);
}
