package com.shopee.demo.infrastructure.dal.data;

import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.strategy.DataSourceEnum;
import com.shopee.demo.engine.type.strategy.StrategyEnum;
import com.shopee.demo.engine.type.strategy.StrategyStatusEnum;
import com.shopee.demo.engine.type.strategy.StrategyTerminalReasonEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UnderwritingFlowDO extends BaseDO{
    private String underwritingId;
    private UnderwritingTypeEnum underwritingType;
    private UnderwritingFlowStatusEnum flowStatus;
    private StrategyEnum currentStrategy;
    private StrategyStatusEnum strategyStatus;
    private DataSourceEnum suspendDataSource;
    private StrategyTerminalReasonEnum terminalReason;
    private String strategyInput;
    private String strategyOutput;
}
