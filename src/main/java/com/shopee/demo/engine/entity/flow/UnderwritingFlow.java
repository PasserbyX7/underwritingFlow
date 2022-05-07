package com.shopee.demo.engine.entity.flow;

import com.shopee.demo.engine.entity.strategy.Strategy;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyEnum;
import com.shopee.demo.engine.type.strategy.StrategyResult;
import com.shopee.demo.engine.type.strategy.StrategyStatusEnum;

import lombok.Data;

@Data
public final class UnderwritingFlow {

    private final Long id;
    private final UnderwritingRequest underwritingRequest;
    private final StrategyContext<UnderwritingRequest> strategyContext;
    private UnderwritingFlowStatusEnum flowStatus;
    private StrategyEnum currentStrategy;

    public UnderwritingFlow(Long id, UnderwritingRequest underwritingRequest,
            StrategyContext<UnderwritingRequest> strategyContext,
            UnderwritingFlowStatusEnum flowStatus, StrategyEnum currentStrategy) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.strategyContext = strategyContext;
        this.flowStatus = flowStatus;
        this.currentStrategy = currentStrategy;
    }

    public static UnderwritingFlow of(UnderwritingRequest underwritingRequest) {
        Long id = null;
        UnderwritingFlowStatusEnum flowStatus = UnderwritingFlowStatusEnum.CREATED;
        StrategyContext<UnderwritingRequest> strategyContext = StrategyContext.of(underwritingRequest);
        return new UnderwritingFlow(id, underwritingRequest, strategyContext, flowStatus, null);
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

    public StrategyStatusEnum getStrategyResultStatus() {
        return strategyContext.getStrategyResult().getStatus();
    }

    public StrategyEnum getCurrentStrategyName() {
        return getCurrentStrategy().getStrategyName();
    }

    private Strategy<UnderwritingRequest> getCurrentStrategy() {
        return currentStrategy != null ? underwritingRequest.getStrategyChain().getStrategy(currentStrategy)
                : underwritingRequest.getStrategyChain().getFirstStrategy();
    }

}
