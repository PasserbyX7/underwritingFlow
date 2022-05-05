package com.shopee.demo.engine.domain.service.strategy;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.domain.entity.StrategyContext;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;
import com.shopee.demo.engine.domain.type.strategy.StrategyResult;

import lombok.Getter;
import lombok.Setter;
public abstract class Strategy<T extends UnderwritingRequest> {
    public abstract StrategyEnum getStrategyName();

    @Setter
    @Getter
    protected Strategy<T> nextStrategy;

    public abstract StrategyResult execute(StrategyContext<T> strategyContext);

}
