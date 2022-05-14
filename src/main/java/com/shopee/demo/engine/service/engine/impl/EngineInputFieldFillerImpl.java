package com.shopee.demo.engine.service.engine.impl;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shopee.demo.engine.service.engine.EngineInputFieldFiller;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.strategy.StrategyInput;

import org.springframework.stereotype.Component;

/**
 * @author li.cao
 * @since 2022-04-21
 */
@Component
public class EngineInputFieldFillerImpl implements EngineInputFieldFiller {

    @Resource
    protected ObjectMapper mapper;

    @Override
    public void fill(StrategyInput source, EngineInput target) {
        setValue(mapper.valueToTree(source), target.getInput());
    }

    protected void setValue(JsonNode source, JsonNode target) {
        Iterator<Entry<String, JsonNode>> iter = source.fields();
        while (iter.hasNext()) {
            Entry<String, JsonNode> entry = iter.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();
            if (fieldValue.isArray()) {
                ArrayNode sourceJsonArray = (ArrayNode) fieldValue;
                if (target.get(fieldName) == null) {
                    throw new IllegalArgumentException(String.format("json property[%s] does not exist", fieldName));
                }
                ArrayNode targetJsonArray = (ArrayNode) target.get(fieldName).get("value");
                int n = sourceJsonArray.size();
                for (int i = 0; i < n - 1; i++) {
                    targetJsonArray.add(targetJsonArray.get(0).deepCopy());
                }
                for (int i = 0; i < n; i++) {
                    setValue(sourceJsonArray.get(i), targetJsonArray.get(i));
                }
            }
            if (fieldValue.isObject()) {
                setValue(source.get(fieldName), target.get(fieldName));
            }
            if (fieldValue.isValueNode()) {
                setValue(target, fieldName, fieldValue);
            }
        }
    }

    protected final void setValue(JsonNode node, String fieldName, JsonNode value) {
        if (node == null || fieldName == null || value.isNull() || "@class".equals(fieldName)) {
            return;
        }
        JsonNode target = node.get(fieldName);
        if (target == null) {
            throw new IllegalArgumentException(String.format("json property[%s] does not exist", fieldName));
        }
        ((ObjectNode) target).set("value", value);
    }

}
