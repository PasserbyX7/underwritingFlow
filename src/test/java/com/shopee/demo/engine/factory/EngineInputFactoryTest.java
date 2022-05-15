package com.shopee.demo.engine.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.infrastructure.utils.JsonUtils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("EngineInputFactoryTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class EngineInputFactoryTest {

    @BeforeAll
    public static void beforeAll() {
        JsonUtils.mapper = new ObjectMapper();
    }

    @Test
    void testCreateSmeEngineInput() throws Exception {
        // given
        EngineInput expected = mockSmeEngineInput("/sme/StrategyInput.json");
        EngineInput actual = EngineInputFactory.createEngineInput(UnderwritingTypeEnum.SME);
        // then
        assertEquals(expected, actual);
    }

    private EngineInput mockSmeEngineInput(String path) throws Exception {
        return EngineInput.of(JsonUtils.readTree(getClass().getResource(path)));
    }

}
