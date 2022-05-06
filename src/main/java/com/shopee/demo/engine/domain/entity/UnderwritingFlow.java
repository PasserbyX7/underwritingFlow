package com.shopee.demo.engine.domain.entity;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.service.strategy.Strategy;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;
import com.shopee.demo.engine.domain.type.strategy.StrategyChain;
import com.shopee.demo.engine.domain.type.strategy.StrategyChainFactory;
import com.shopee.demo.engine.domain.type.strategy.StrategyResult;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
public final class UnderwritingFlow<T extends UnderwritingRequest> {

    private Long id;
    private T underwritingRequest;
    private StrategyContext<T> strategyContext;
    @Setter
    @NonFinal
    private UnderwritingFlowStatusEnum flowStatus;
    @NonFinal
    private StrategyEnum currentStrategy;
    private StrategyChain<T> strategyChain;

    public UnderwritingFlow(Long id, T underwritingRequest, StrategyContext<T> strategyContext,
            UnderwritingFlowStatusEnum flowStatus, StrategyEnum currentStrategy) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.strategyContext = strategyContext;
        this.flowStatus = flowStatus;
        this.strategyChain = StrategyChainFactory.create(underwritingRequest.getUnderwritingType());
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
