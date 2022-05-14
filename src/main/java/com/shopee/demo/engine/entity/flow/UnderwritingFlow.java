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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(onlyExplicitlyIncluded = true)
public final class UnderwritingFlow {

    public static final String EXTENDED_STATE_KEY = "UnderwritingFlowExtendedStateKey";

    @ToString.Include
    private final Long id;
    private final UnderwritingRequest underwritingRequest;
    private final StrategyContext<UnderwritingRequest> strategyContext;
    @ToString.Include
    private FlowStatusEnum flowStatus;
    @ToString.Include
    private StrategyEnum currentStrategyName;

    public UnderwritingFlow(Long id, UnderwritingRequest underwritingRequest,
            StrategyContext<UnderwritingRequest> strategyContext,
            FlowStatusEnum flowStatus, StrategyEnum currentStrategy) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.strategyContext = strategyContext;
        this.flowStatus = flowStatus;
        this.currentStrategyName = currentStrategy;
    }

    public static UnderwritingFlow of(UnderwritingRequest underwritingRequest) {
        Assert.notNull(underwritingRequest, "UnderwritingRequest must not be null");
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
        currentStrategyName = getCurrentStrategy().getStrategyName();
        StrategyResult strategyResult = getCurrentStrategy().execute(strategyContext);
        strategyContext.setStrategyResult(strategyResult);
        log.info("flow[{}] execute [{}] with result [{}]", id, currentStrategyName, strategyResult);
    }

    public void setNextStrategy() {
        if (hasNextStrategy()) {
            this.currentStrategyName = getCurrentStrategy().getNextStrategy().getStrategyName();
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

    private Strategy<UnderwritingRequest> getCurrentStrategy() {
        return currentStrategyName != null ? underwritingRequest.getStrategyChain().getStrategy(currentStrategyName)
                : underwritingRequest.getStrategyChain().getFirstStrategy();
    }

}
