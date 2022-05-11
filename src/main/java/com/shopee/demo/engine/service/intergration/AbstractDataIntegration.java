package com.shopee.demo.engine.service.intergration;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.service.provider.StrategyInputProvider;
import com.shopee.demo.engine.service.strategy.EngineInputFieldFiller;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyInput;

import org.springframework.stereotype.Service;

@Service
public abstract class AbstractDataIntegration<T extends UnderwritingRequest> implements DataIntegration<T> {

    @Resource
    private EngineInputFieldFiller engineInputFieldFiller;

    @Override
    public final void integration(StrategyContext<T> strategyContext, EngineInput engineInput) {
        StrategyInput strategyInput = getProvider().provide(strategyContext);
        engineInputFieldFiller.fill(strategyInput, engineInput);
    }

    protected abstract StrategyInputProvider<T> getProvider();

}