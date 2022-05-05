package com.shopee.demo.domain.entity;

import java.util.concurrent.CountDownLatch;

import com.shopee.demo.constant.StrategyEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.domain.type.strategy.StrategyChain;
import com.shopee.demo.domain.type.strategy.StrategyChainFactory;
import com.shopee.demo.domain.type.strategy.StrategyResult;
import com.shopee.demo.strategy.Strategy;

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
    private CountDownLatch latch;

    public UnderwritingFlow(Long id, T underwritingRequest, StrategyContext<T> strategyContext,
            UnderwritingFlowStatusEnum flowStatus, StrategyEnum currentStrategy) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.strategyContext = strategyContext;
        this.flowStatus = flowStatus;
        this.strategyChain = StrategyChainFactory.create(underwritingRequest.getUnderwritingType());
        this.currentStrategy = currentStrategy != null ? currentStrategy
                : strategyChain.getFirstStrategy().getStrategyName();
        this.latch = new CountDownLatch(1);
    }

    public static <T extends UnderwritingRequest> UnderwritingFlow<T> of(T underwritingRequest) {
        Long id = null;
        UnderwritingFlowStatusEnum flowStatus = UnderwritingFlowStatusEnum.CREATED;
        StrategyContext<T> strategyContext = StrategyContext.of(underwritingRequest);
        return new UnderwritingFlow<T>(id, underwritingRequest, strategyContext, flowStatus, null);
    }

    public void execute() {
        StrategyResult strategyResult = getCurrentStrategy().execute(getStrategyContext());
        strategyContext.setStrategyResult(strategyResult);
    }

    public void setNextStrategy() {
        if (hasNextStrategy()) {
            this.currentStrategy = getCurrentStrategy().getNextStrategy().getStrategyName();
        }
    }

    public StrategyResult getStrategyResult() {
        return strategyContext.getStrategyResult();
    }

    public boolean hasNextStrategy() {
        return getCurrentStrategy().getNextStrategy() != null;
    }

    public StrategyEnum getCurrentStrategyName() {
        return getCurrentStrategy().getStrategyName();
    }

    private Strategy<T> getCurrentStrategy() {
        return strategyChain.getStrategy(currentStrategy);
    }

}
