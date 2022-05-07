package com.shopee.demo.engine.type.strategy;

import java.util.HashMap;
import java.util.Map;

public class StrategyContainer<T> {

    private final Map<StrategyEnum, T> strategyContainer = new HashMap<>();

    public T get(StrategyEnum strategyEnum) {
        return strategyContainer.get(strategyEnum);
    }

    public void put(StrategyEnum strategyEnum, T value) {
        strategyContainer.put(strategyEnum, value);
    }

    public String toJson() {
        // TODO
        return null;
    }

    public static <T> StrategyContainer<T> fromJson(String str) {
        // TODO
        return null;
    }
}
