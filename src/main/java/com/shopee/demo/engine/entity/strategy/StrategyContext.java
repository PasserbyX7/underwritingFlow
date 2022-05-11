package com.shopee.demo.engine.entity.strategy;

import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.engine.EngineOutput;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyContainer;
import com.shopee.demo.engine.type.strategy.StrategyResult;

import lombok.Data;

@Data
public class StrategyContext<T extends UnderwritingRequest> {
    private final T underwritingRequest;
    private StrategyResult strategyResult;
    private final StrategyContainer<EngineInput> strategyInput;
    private final StrategyContainer<EngineOutput> strategyOutput;

    public static <T extends UnderwritingRequest> StrategyContext<T> of(T underwritingRequest) {
        StrategyContainer<EngineInput> strategyInput = new StrategyContainer<>();
        StrategyContainer<EngineOutput> strategyOutput = new StrategyContainer<>();
        return new StrategyContext<T>(underwritingRequest, strategyInput, strategyOutput);
    }
}
