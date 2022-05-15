package com.shopee.demo.engine.repository.converter;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.engine.EngineOutput;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyContainer;
import com.shopee.demo.engine.type.strategy.StrategyResult;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;
import com.shopee.demo.infrastructure.utils.JsonUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingFlowConverterTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingFlowConverterTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void testConvertFlowDO2Flow() throws Exception {
        // given
        JsonUtils.mapper = mapper;
        UnderwritingFlow expected = mockUnderwritingFlow();
        UnderwritingFlowDO flowDO = mockUnderwritingFlowDO();
        UnderwritingRequest request = mockUnderwritingRequest();
        // when
        UnderwritingFlow actual = UnderwritingFlowConverter.convert(flowDO, request);
        // then
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUnderwritingRequest().getUnderwritingId(),
                actual.getUnderwritingRequest().getUnderwritingId());
        assertEquals(expected.getUnderwritingRequest().getUnderwritingType(),
                actual.getUnderwritingRequest().getUnderwritingType());
        assertEquals(expected.getFlowStatus(), actual.getFlowStatus());
        assertEquals(expected.getCurrentStrategyName(), actual.getCurrentStrategyName());
        assertEquals(expected.getStrategyResult(),
                actual.getStrategyResult());
        assertEquals(expected.getStrategyContext().getStrategyInput(),
                actual.getStrategyContext().getStrategyInput());
        assertEquals(expected.getStrategyContext().getStrategyOutput(),
                actual.getStrategyContext().getStrategyOutput());
    }

    @Test
    void testConvertFlow2FlowDO() throws Exception {
        // given
        JsonUtils.mapper = mapper;
        UnderwritingFlowDO expected = mockUnderwritingFlowDO();
        UnderwritingFlow flow = mockUnderwritingFlow();
        // when
        UnderwritingFlowDO actual = UnderwritingFlowConverter.convert(flow);
        // then
        assertEquals(expected, actual);
    }

    private UnderwritingFlowDO mockUnderwritingFlowDO() throws Exception {
        UnderwritingFlowDO flowDO = new UnderwritingFlowDO();
        flowDO.setId(1L);
        flowDO.setUnderwritingId("underwritingId");
        flowDO.setUnderwritingType(UnderwritingTypeEnum.SME);
        flowDO.setFlowStatus(FlowStatusEnum.ONGOING);
        flowDO.setCurrentStrategy(StrategyEnum.SME_STRATEGY_1);
        flowDO.setStrategyStatus(StrategyStatusEnum.SUSPEND);
        flowDO.setSuspendDataSource(DataSourceEnum.SELLER);
        flowDO.setStrategyInput(mockStrategyContainerInput().toJson());
        flowDO.setStrategyOutput(mockStrategyContainerOutput().toJson());
        return flowDO;
    }

    private UnderwritingFlow mockUnderwritingFlow() throws Exception {
        long flowId = 1L;
        UnderwritingRequest underwritingRequest = mockUnderwritingRequest();
        FlowStatusEnum flowStatus = FlowStatusEnum.ONGOING;
        StrategyEnum currentStrategy = StrategyEnum.SME_STRATEGY_1;
        StrategyContext<UnderwritingRequest> strategyContext = StrategyContext.of(underwritingRequest,
                mockStrategyContainerInput(),
                mockStrategyContainerOutput());
        return new UnderwritingFlow(flowId, underwritingRequest, strategyContext,
                StrategyResult.suspend(DataSourceEnum.SELLER), flowStatus, currentStrategy);
    }

    private UnderwritingRequest mockUnderwritingRequest() {
        return new UnderwritingRequest() {

            @Override
            public UnderwritingTypeEnum getUnderwritingType() {
                return UnderwritingTypeEnum.SME;
            }

            @Override
            public String getUnderwritingId() {
                return "underwritingId";
            }

            @Override
            public boolean isExpire() {
                return false;
            }
        };
    }

    private StrategyContainer<EngineInput> mockStrategyContainerInput() throws Exception {
        String json = "{\"k1\":\"v1\"}";
        StrategyContainer<EngineInput> container = new StrategyContainer<>();
        container.put(StrategyEnum.SME_STRATEGY_1, EngineInput.of(mapper.readTree(json)));
        return container;
    }

    private StrategyContainer<EngineOutput> mockStrategyContainerOutput() throws Exception {
        String json = "{\"k1\":\"v1\"}";
        StrategyContainer<EngineOutput> container = new StrategyContainer<>();
        container.put(StrategyEnum.SME_STRATEGY_1, EngineOutput.of(mapper.readTree(json)));
        return container;
    }

}
