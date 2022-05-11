package com.shopee.demo.engine.factory;

import java.io.IOException;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.engine.EngineInput;

import org.springframework.stereotype.Component;

/**
 * @author li.cao
 * @since 2022-04-21
 */
@Component
public class EngineInputFactory {

    @Resource
    private SmeEngineInputFactory smeEngineInputFactory;

    public EngineInput createSmeEngineInput(UnderwritingTypeEnum underwritingType) {
        if (underwritingType == UnderwritingTypeEnum.SME) {
            return smeEngineInputFactory.create();
        }
        throw new RuntimeException();
    }

    @Component
    public static class SmeEngineInputFactory {
        private final JsonNode root;

        public SmeEngineInputFactory(ObjectMapper mapper) throws IOException {
            root = mapper.readTree(getClass().getResource("/sme/StrategyInput.json"));
        }

        public EngineInput create() {
            return EngineInput.of(root.deepCopy());
        }
    }

}
