package com.shopee.demo.engine.type.strategy;

import javax.annotation.PostConstruct;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

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
