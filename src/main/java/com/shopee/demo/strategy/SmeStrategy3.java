package com.shopee.demo.strategy;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.type.request.SmeUnderwritingRequest;

import org.springframework.stereotype.Component;

@Component
public class SmeStrategy3 extends Strategy<SmeUnderwritingRequest> {

    @Override
    public StrategyEnum getStrategyName() {
        return StrategyEnum.SME_STRATEGY_3;
    }

}
