package com.shopee.demo.infrastructure.data;

import java.util.Map;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.domain.type.input.StrategyInput;
import com.shopee.demo.domain.type.output.StrategyOutput;

import lombok.Data;

@Data
public class UnderwritingFlowDO {
    private Long id;
    private String underwritingId;
    private Long requestTime;
    private StrategyEnum currentStrategy;
    private Map<StrategyEnum, StrategyInput> strategyInput;
    private Map<StrategyEnum, StrategyOutput> strategyOutput;
    private UnderwritingFlowStatusEnum status;
    private UnderwritingTypeEnum type;
}
