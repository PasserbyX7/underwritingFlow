package com.shopee.demo.infrastructure.dal.data;

import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.constant.StrategyTerminalReasonEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;

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
