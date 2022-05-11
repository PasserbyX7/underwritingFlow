package com.shopee.demo.infrastructure.dal.handler;

import java.util.EnumMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.type.strategy.StrategyInput;


public class StrategyInputTypeHandler extends AbstractJsonTypeHandler<Map<StrategyEnum, StrategyInput>> {

    private static ObjectMapper OBJECT_MAPPER;

    public static ObjectMapper getObjectMapper() {
        if (null == OBJECT_MAPPER)
            OBJECT_MAPPER = new ObjectMapper();
        return OBJECT_MAPPER;
    }

    @Override
    protected Map<StrategyEnum, StrategyInput> parse(String json) {
        try {
            Map<StrategyEnum, StrategyInput> obj = new EnumMap<>(StrategyEnum.class);
            Map<StrategyEnum, String> map = getObjectMapper().readValue(json,
                    new TypeReference<Map<StrategyEnum, String>>() {
                    });
            for (Map.Entry<StrategyEnum, String> entry : map.entrySet()) {
                obj.put(entry.getKey(), getObjectMapper().readValue(entry.getValue(), StrategyInput.class));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(Map<StrategyEnum, StrategyInput> obj) {
        try {
            Map<StrategyEnum, String> map = new EnumMap<>(StrategyEnum.class);
            for (Map.Entry<StrategyEnum, StrategyInput> entry : obj.entrySet()) {
                map.put(entry.getKey(), getObjectMapper().writeValueAsString(entry.getValue()));
            }
            return getObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
