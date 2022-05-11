package com.shopee.demo.infrastructure.dal.data;

import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnderwritingFlowLogDO extends BaseDO {
    private Long underwritingFlowId;
    private FlowStatusEnum flowStatus;
    private StrategyEnum currentStrategy;
    private StrategyStatusEnum strategyStatus;
    private DataSourceEnum suspendDataSource;
    private String errorMsg;
    private String strategyInput;
    private String strategyOutput;
}
