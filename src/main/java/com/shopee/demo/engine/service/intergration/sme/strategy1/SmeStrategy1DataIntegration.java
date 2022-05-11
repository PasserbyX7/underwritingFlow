package com.shopee.demo.engine.service.intergration.sme.strategy1;

import com.shopee.demo.engine.service.intergration.AbstractDataIntegration;
import com.shopee.demo.engine.service.provider.StrategyInputProvider;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;

import org.springframework.stereotype.Service;

@Service
public class SmeStrategy1DataIntegration extends AbstractDataIntegration<SmeUnderwritingRequest> {

    @Override
    protected StrategyInputProvider<SmeUnderwritingRequest> getProvider() {
        return null;
    }

}
