package com.shopee.demo.infrastructure.data;

import com.shopee.demo.constant.DataSourceEnum;
import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.constant.StrategyStatusEnum;
import com.shopee.demo.constant.StrategyTerminalReasonEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.constant.UnderwritingTypeEnum;

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
