package com.shopee.demo.strategy;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.SmeUnderwritingRequest;

import org.springframework.stereotype.Component;

@Component
public class SmeStrategy1 extends Strategy<SmeUnderwritingRequest> {

    @Override
    public StrategyEnum getStrategyName() {
        return StrategyEnum.SME_STRATEGY_1;
    }

}
