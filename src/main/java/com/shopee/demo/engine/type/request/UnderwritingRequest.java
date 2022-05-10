package com.shopee.demo.engine.type.request;

import com.shopee.demo.engine.type.factory.StrategyChainFactory;
import com.shopee.demo.engine.type.strategy.StrategyChain;

public interface UnderwritingRequest {

    UnderwritingTypeEnum getUnderwritingType();

    String getUnderwritingId();

    boolean isExpire();

    default StrategyChain<UnderwritingRequest> getStrategyChain() {
        return StrategyChainFactory.getStrategyChain(getUnderwritingType());
    }

}
