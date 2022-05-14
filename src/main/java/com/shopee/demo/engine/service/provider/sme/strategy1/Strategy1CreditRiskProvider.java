package com.shopee.demo.engine.service.provider.sme.strategy1;

import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.service.provider.StrategyInputProvider;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyInput;
import com.shopee.demo.engine.type.strategy.sme.input.strategy1.SmeStrategy1CreditRiskInput;

import org.springframework.stereotype.Component;

@Component
public class Strategy1CreditRiskProvider implements StrategyInputProvider<SmeUnderwritingRequest> {

    @Override
    public StrategyInput provide(StrategyContext<SmeUnderwritingRequest> strategyContext) {
        return new SmeStrategy1CreditRiskInput();
    }

}
