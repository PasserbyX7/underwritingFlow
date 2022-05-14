package com.shopee.demo.engine.type.strategy;

import java.util.HashMap;
import java.util.Map;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.infrastructure.utils.JsonUtils;

import lombok.Data;

@Data
public class StrategyContainer<T> {

    private Map<StrategyEnum, T> strategyContainer = new HashMap<>();

    public T get(StrategyEnum strategyEnum) {
        return strategyContainer.get(strategyEnum);
    }

    public void put(StrategyEnum strategyEnum, T value) {
        strategyContainer.put(strategyEnum, value);
    }

    public String toJson() {
        return JsonUtils.writeValueAsString(this);
    }

}
