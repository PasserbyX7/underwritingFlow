package com.shopee.demo.entity;

import java.util.Map;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.strategy.StrategyInput;
import com.shopee.demo.strategy.StrategyOutput;

import lombok.Data;

@Data
public class UnderwritingContextDO {
    private Long id;
    private String underwritingId;
    private Long requestTime;
    private StrategyEnum currentStrategy;
    private Map<StrategyEnum, StrategyInput> strategyInput;
    private Map<StrategyEnum, StrategyOutput> strategyOutput;
    private UnderwritingFlowStatusEnum status;
    private UnderwritingTypeEnum type;
}
