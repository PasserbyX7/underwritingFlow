package com.shopee.demo.context;

import java.util.Map;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.UnderwritingRequest;
import com.shopee.demo.strategy.StrategyInput;
import com.shopee.demo.strategy.StrategyOutput;
import com.shopee.demo.strategy.StrategyResult;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StrategyContext<T extends UnderwritingRequest> {
    private final T UnderwritingBaseDO;
    private StrategyEnum currentStrategy;
    private StrategyResult strategyResult;
    private Map<StrategyEnum, StrategyInput> strategyInput;
    private Map<StrategyEnum, StrategyOutput> strategyOutput;
}
