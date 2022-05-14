package com.shopee.demo.engine.service.intergration.sme.strategy1;

import javax.annotation.Resource;

import com.shopee.demo.engine.service.intergration.AbstractDataIntegration;
import com.shopee.demo.engine.service.provider.StrategyInputProvider;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;

import org.springframework.stereotype.Service;

@Service
public class CreditRiskIntegration extends AbstractDataIntegration<SmeUnderwritingRequest> {

    @Resource
    private StrategyInputProvider<SmeUnderwritingRequest> strategy1CreditRiskProvider;

    @Override
    protected StrategyInputProvider<SmeUnderwritingRequest> getProvider() {
        return strategy1CreditRiskProvider;
    }

}
