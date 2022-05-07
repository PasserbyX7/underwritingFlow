package com.shopee.demo.engine.entity.flow;

import com.shopee.demo.engine.entity.strategy.Strategy;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.factory.StrategyChainFactory;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyChain;
import com.shopee.demo.engine.type.strategy.StrategyEnum;
import com.shopee.demo.engine.type.strategy.StrategyResult;
import com.shopee.demo.engine.type.strategy.StrategyStatusEnum;

import lombok.Data;

@Data
public final class UnderwritingFlow<T extends UnderwritingRequest> {

    private final Long id;
    private final T underwritingRequest;
    private final StrategyContext<T> strategyContext;
    private UnderwritingFlowStatusEnum flowStatus;
    private StrategyEnum currentStrategy;
    private final StrategyChain<T> strategyChain;

    public UnderwritingFlow(Long id, T underwritingRequest, StrategyContext<T> strategyContext,
            UnderwritingFlowStatusEnum flowStatus, StrategyEnum currentStrategy) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.strategyContext = strategyContext;
        this.flowStatus = flowStatus;
        this.strategyChain = StrategyChainFactory.getStrategyChain(underwritingRequest.getUnderwritingType());
        this.currentStrategy = currentStrategy != null ? currentStrategy
                : strategyChain.getFirstStrategy().getStrategyName();
    }

    public static <T extends UnderwritingRequest> UnderwritingFlow<T> of(T underwritingRequest) {
        Long id = null;
        UnderwritingFlowStatusEnum flowStatus = UnderwritingFlowStatusEnum.CREATED;
        StrategyContext<T> strategyContext = StrategyContext.of(underwritingRequest);
        return new UnderwritingFlow<T>(id, underwritingRequest, strategyContext, flowStatus, null);
    }

    public void execute() {
        StrategyResult strategyResult = getCurrentStrategy().execute(strategyContext);
        strategyContext.setStrategyResult(strategyResult);
    }

    public void setNextStrategy() {
        if (hasNextStrategy()) {
            this.currentStrategy = getCurrentStrategy().getNextStrategy().getStrategyName();
        }
    }

    public boolean hasNextStrategy() {
        return getCurrentStrategy().getNextStrategy() != null;
    }

    public StrategyStatusEnum getStrategyResultStatus(){
        return strategyContext.getStrategyResult().getStatus();
    }

    public StrategyEnum getCurrentStrategyName() {
        return getCurrentStrategy().getStrategyName();
    }

    private Strategy<T> getCurrentStrategy() {
        return strategyChain.getStrategy(currentStrategy);
    }

}
