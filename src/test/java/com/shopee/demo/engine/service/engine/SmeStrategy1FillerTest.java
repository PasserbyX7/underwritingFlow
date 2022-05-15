package com.shopee.demo.engine.service.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.shopee.demo.engine.constant.input.BoolEnum;
import com.shopee.demo.engine.service.engine.impl.EngineInputFieldFillerImpl;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.strategy.sme.input.strategy1.SmeStrategy1CreditRiskInput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * @author li.cao
 * @since 2022-04-21
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SmeStrategy1FillerTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class SmeStrategy1FillerTest {

    @InjectMocks
    private EngineInputFieldFillerImpl creditRiskFiller;

    @Spy
    private ObjectMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreditRiskFill() throws Exception {
        // given
        SmeStrategy1CreditRiskInput input = mockCreditRiskInput();
        EngineInput actual = mockSmeEngineInput("/sme/StrategyInput.json");
        EngineInput expected = mockSmeEngineInput("/sme/input/strategy1/SmeStrategy1CreditRisk.json");
        // then
        creditRiskFiller.fill(input, actual);
        assertEquals(expected.convertToJson(), actual.convertToJson());
    }

    private SmeStrategy1CreditRiskInput mockCreditRiskInput() {
        SmeStrategy1CreditRiskInput input = new SmeStrategy1CreditRiskInput();
        // set uw
        SmeStrategy1CreditRiskInput.UnderwritingInfo underwritingInfo = new SmeStrategy1CreditRiskInput.UnderwritingInfo();
        underwritingInfo.setUnderwritingRequestId("underwritingRequestId");
        input.setUnderwritingInfo(underwritingInfo);
        // set corp
        SmeStrategy1CreditRiskInput.CorpInfo corpInfo = new SmeStrategy1CreditRiskInput.CorpInfo();
        corpInfo.setBlackListFlag(BoolEnum.YES);
        corpInfo.setOverdueCount(1);
        corpInfo.setOverdue60Count(2);
        corpInfo.setMaxOverdueDay(3);
        input.setCorpInfo(corpInfo);
        // set guar
        SmeStrategy1CreditRiskInput.GuarInfo guarInfo1 = new SmeStrategy1CreditRiskInput.GuarInfo();
        guarInfo1.setBlackListFlag(BoolEnum.YES);
        SmeStrategy1CreditRiskInput.GuarInfo guarInfo2 = new SmeStrategy1CreditRiskInput.GuarInfo();
        guarInfo2.setBlackListFlag(BoolEnum.YES);
        input.setGuarInfoList(Lists.newArrayList(guarInfo1, guarInfo2));
        // return
        return input;
    }

    private EngineInput mockSmeEngineInput(String path) throws Exception {
        return EngineInput.of(mapper.readTree(getClass().getResource(path)));
    }
}
