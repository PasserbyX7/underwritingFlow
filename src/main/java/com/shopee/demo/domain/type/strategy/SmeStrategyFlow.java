package com.shopee.demo.domain.type.strategy;

import javax.annotation.Resource;

import com.shopee.demo.domain.type.request.SmeUnderwritingRequest;
import com.shopee.demo.strategy.Strategy;

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
