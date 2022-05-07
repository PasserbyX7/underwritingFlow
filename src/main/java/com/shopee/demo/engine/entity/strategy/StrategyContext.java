package com.shopee.demo.engine.entity.strategy;

import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyContainer;
import com.shopee.demo.engine.type.strategy.StrategyResult;
import com.shopee.demo.engine.type.strategy.input.StrategyInput;
import com.shopee.demo.engine.type.strategy.output.StrategyOutput;

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
