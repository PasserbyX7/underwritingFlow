package com.shopee.demo.engine.type.strategy;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.strategy.Strategy;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;

import org.springframework.stereotype.Component;

@Component
public class SmeStrategyFlow extends AbstractStrategyChain<SmeUnderwritingRequest> {

    @Resource
    private Strategy<SmeUnderwritingRequest> smeStrategy1;

    @Resource
    private Strategy<SmeUnderwritingRequest> smeStrategy2;

    @Resource
    private Strategy<SmeUnderwritingRequest> smeStrategy3;

    @Override
    public void configStrategyChain() {
        smeStrategy1.setNextStrategy(smeStrategy2);
        smeStrategy2.setNextStrategy(smeStrategy3);
    }

    @Override
    public Strategy<SmeUnderwritingRequest> getFirstStrategy() {
        return smeStrategy1;
    }

}
