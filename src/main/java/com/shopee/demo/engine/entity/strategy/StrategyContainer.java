package com.shopee.demo.engine.entity.strategy;

import java.util.HashMap;
import java.util.Map;

import com.shopee.demo.engine.type.strategy.StrategyEnum;

public class StrategyContainer<T> {

    private final Map<StrategyEnum, T> strategyContainer = new HashMap<>();

    public T get(StrategyEnum strategyEnum) {
        return strategyContainer.get(strategyEnum);
    }

    public void put(StrategyEnum strategyEnum, T value) {
        strategyContainer.put(strategyEnum, value);
    }

}
