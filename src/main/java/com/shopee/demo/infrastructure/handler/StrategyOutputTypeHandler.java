package com.shopee.demo.infrastructure.handler;

import java.util.EnumMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.domain.type.output.StrategyOutput;

public class StrategyOutputTypeHandler extends AbstractJsonTypeHandler<Map<StrategyEnum, StrategyOutput>> {

    private static ObjectMapper OBJECT_MAPPER;

    public static ObjectMapper getObjectMapper() {
        if (null == OBJECT_MAPPER)
            OBJECT_MAPPER = new ObjectMapper();
        return OBJECT_MAPPER;
    }

    @Override
    protected Map<StrategyEnum, StrategyOutput> parse(String json) {
        try {
            Map<StrategyEnum, StrategyOutput> obj = new EnumMap<>(StrategyEnum.class);
            Map<StrategyEnum, String> map = getObjectMapper().readValue(json,
                    new TypeReference<Map<StrategyEnum, String>>() {
                    });
            for (Map.Entry<StrategyEnum, String> entry : map.entrySet()) {
                obj.put(entry.getKey(), getObjectMapper().readValue(entry.getValue(), StrategyOutput.class));
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(Map<StrategyEnum, StrategyOutput> obj) {
        try {
            Map<StrategyEnum, String> map = new EnumMap<>(StrategyEnum.class);
            for (Map.Entry<StrategyEnum, StrategyOutput> entry : obj.entrySet()) {
                map.put(entry.getKey(), getObjectMapper().writeValueAsString(entry.getValue()));
            }
            return getObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}