package com.shopee.demo.domain.entity;

import java.util.Map;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.domain.type.input.StrategyInput;
import com.shopee.demo.domain.type.output.StrategyOutput;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.domain.type.strategy.StrategyResult;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StrategyContext<T extends UnderwritingRequest> {
    private final T underwritingRequest;
    private StrategyEnum currentStrategy;
    private StrategyResult strategyResult;
    private Map<StrategyEnum, StrategyInput> strategyInput;
    private Map<StrategyEnum, StrategyOutput> strategyOutput;
}
