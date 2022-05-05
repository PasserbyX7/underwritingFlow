package com.shopee.demo.domain.entity;

import com.shopee.demo.domain.type.input.StrategyInput;
import com.shopee.demo.domain.type.output.StrategyOutput;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.domain.type.strategy.StrategyResult;

import lombok.Data;

@Data
public class StrategyContext<T extends UnderwritingRequest> {
    private final T underwritingRequest;
    private StrategyResult strategyResult;
    private final StrategyContainer<StrategyInput> strategyInput;
    private final StrategyContainer<StrategyOutput> strategyOutput;

    public static <T extends UnderwritingRequest> StrategyContext<T> of(T underwritingRequest) {
        StrategyContainer<StrategyInput> strategyInput = new StrategyContainer<>();
        StrategyContainer<StrategyOutput> strategyOutput = new StrategyContainer<>();
        return new StrategyContext<T>(underwritingRequest, strategyInput, strategyOutput);
    }
}
