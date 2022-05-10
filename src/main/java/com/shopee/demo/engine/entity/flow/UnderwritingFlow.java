package com.shopee.demo.engine.entity.flow;

import java.util.Optional;

import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.Strategy;
import com.shopee.demo.engine.type.strategy.StrategyResult;

import org.springframework.statemachine.ExtendedState;
import org.springframework.util.Assert;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public final class UnderwritingFlow {

    public static final String EXTENDED_STATE_KEY = "UnderwritingFlowExtendedStateKey";

    private final Long id;
    private final UnderwritingRequest underwritingRequest;
    private final StrategyContext<UnderwritingRequest> strategyContext;
    private FlowStatusEnum flowStatus;
    private StrategyEnum currentStrategy;

    public UnderwritingFlow(Long id, UnderwritingRequest underwritingRequest,
            StrategyContext<UnderwritingRequest> strategyContext,
            FlowStatusEnum flowStatus, StrategyEnum currentStrategy) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.strategyContext = strategyContext;
        this.flowStatus = flowStatus;
        this.currentStrategy = currentStrategy;
    }

    public static UnderwritingFlow of(UnderwritingRequest underwritingRequest) {
        Long id = null;
        FlowStatusEnum flowStatus = FlowStatusEnum.INITIAL;
        StrategyContext<UnderwritingRequest> strategyContext = StrategyContext.of(underwritingRequest);
        return new UnderwritingFlow(id, underwritingRequest, strategyContext, flowStatus, null);
    }

    public static UnderwritingFlow from(ExtendedState extendedState) {
        Assert.notNull(extendedState, "ExtendedState must not be null");
        return extendedState.get(UnderwritingFlow.EXTENDED_STATE_KEY, UnderwritingFlow.class);
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
        return getCurrentStrategy().hasNextStrategy();
    }

    public StrategyStatusEnum getStrategyResultStatus() {
        return Optional.ofNullable(strategyContext)
                .map(StrategyContext::getStrategyResult)
                .map(StrategyResult::getStatus)
                .orElse(null);
    }

    public StrategyEnum getCurrentStrategyName() {
        return currentStrategy;
    }

    private Strategy<UnderwritingRequest> getCurrentStrategy() {
        return currentStrategy != null ? underwritingRequest.getStrategyChain().getStrategy(currentStrategy)
                : underwritingRequest.getStrategyChain().getFirstStrategy();
    }

}
