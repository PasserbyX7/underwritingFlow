package com.shopee.demo.engine.repository.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlowLog;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingFlowLogConverterTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingFlowLogConverterTest {

    @Test
    void testFlowDO2FlowLog() throws Exception {
        UnderwritingFlowDO flowDO = mockUnderwritingFlowDO();
        UnderwritingFlowLog flowLog = mockUnderwritingFlowLog();
        assertEquals(flowLog, UnderwritingFlowLogConverter.INSTANCE.convert(flowDO));
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
        flowDO.setErrorMsg("errorMsg");
        flowDO.setStrategyInput("input");
        flowDO.setStrategyOutput("output");
        return flowDO;
    }

    private UnderwritingFlowLog mockUnderwritingFlowLog() {
        UnderwritingFlowLog flowLog = new UnderwritingFlowLog();
        flowLog.setUnderwritingFlowId(1L);
        flowLog.setFlowStatus(FlowStatusEnum.ONGOING);
        flowLog.setCurrentStrategy(StrategyEnum.SME_STRATEGY_1);
        flowLog.setStrategyStatus(StrategyStatusEnum.SUSPEND);
        flowLog.setSuspendDataSource(DataSourceEnum.SELLER);
        flowLog.setErrorMsg("errorMsg");
        flowLog.setStrategyInput("input");
        flowLog.setStrategyOutput("output");
        return flowLog;
    }
}
