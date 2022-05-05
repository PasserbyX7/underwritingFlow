package com.shopee.demo.domain.entity;

import java.util.HashMap;
import java.util.Map;

import com.shopee.demo.constant.StrategyEnum;

public class StrategyContainer<T> {

    private final Map<StrategyEnum, T> strategyContainer = new HashMap<>();

    public T get(StrategyEnum strategyEnum) {
        return strategyContainer.get(strategyEnum);
    }

    public void put(StrategyEnum strategyEnum, T value) {
        strategyContainer.put(strategyEnum, value);
    }

}
