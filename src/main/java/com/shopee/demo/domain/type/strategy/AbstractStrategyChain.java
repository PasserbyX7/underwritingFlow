package com.shopee.demo.domain.type.strategy;

import javax.annotation.PostConstruct;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.strategy.Strategy;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractStrategyChain<T extends UnderwritingRequest> implements StrategyChain<T> {

    @PostConstruct
    protected abstract void configStrategyChain();

    @Override
    public Strategy<T> getStrategy(StrategyEnum strategyName) {
        Strategy<T> head = getFirstStrategy();
        while (head != null) {
            if (head.getStrategyName() == strategyName) {
                return head;
            }
            head = head.getNextStrategy();
        }
        return null;
    }

}
