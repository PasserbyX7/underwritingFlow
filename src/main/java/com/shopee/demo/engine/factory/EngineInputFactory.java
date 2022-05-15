package com.shopee.demo.engine.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.infrastructure.utils.JsonUtils;

public class EngineInputFactory {

    public static EngineInput createEngineInput(UnderwritingTypeEnum underwritingType) {
        if (underwritingType == UnderwritingTypeEnum.SME) {
            return getSmeEngineInputFactory().create();
        }
        throw new IllegalArgumentException("undefined underwriting type");
    }

    public static SmeEngineInputFactory getSmeEngineInputFactory() {
        return SmeEngineInputFactory.instance;
    }

    public static class SmeEngineInputFactory {
        private static final SmeEngineInputFactory instance = new SmeEngineInputFactory();
        private final JsonNode root;

        public SmeEngineInputFactory() {
            root = JsonUtils.readTree(getClass().getResource("/sme/StrategyInput.json"));
        }

        public EngineInput create() {
            return EngineInput.of(root.deepCopy());
        }
    }

}
