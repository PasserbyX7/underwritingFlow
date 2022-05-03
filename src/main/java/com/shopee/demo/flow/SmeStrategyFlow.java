package com.shopee.demo.flow;

import javax.annotation.Resource;

import com.shopee.demo.domain.type.request.SmeUnderwritingRequest;
import com.shopee.demo.strategy.Strategy;

import org.springframework.stereotype.Component;

@Component
public class SmeStrategyFlow extends StrategyFlow<SmeUnderwritingRequest> {

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

    // @Override
    // protected void onFlowPending() {
    //     log.info("flow pending callback");
    // }

    // @Override
    // protected void onFlowApproved() {
    //     log.info("flow approved callback");
    // }

    // @Override
    // protected void onFlowReject() {
    //     log.info("flow reject callback");
    // }

    // @Override
    // protected void onFlowCancelled() {
    //     log.info("flow canceled callback");
    // }

}
