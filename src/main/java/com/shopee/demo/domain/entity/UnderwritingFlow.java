package com.shopee.demo.domain.entity;

import java.util.concurrent.CountDownLatch;

import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.domain.type.request.UnderwritingRequest;
import com.shopee.demo.domain.type.strategy.StrategyResult;
import com.shopee.demo.flow.StrategyFlow;
import com.shopee.demo.strategy.Strategy;

import lombok.Data;

@Data
public final class UnderwritingFlow<T extends UnderwritingRequest> {

    private final Long id;
    private final T underwritingRequest;
    private final StrategyFlow<T> flow;
    private StrategyContext<T> strategyContext;
    private UnderwritingFlowStatusEnum flowStatus;

    private final CountDownLatch latch = new CountDownLatch(1);

    public UnderwritingFlow(Long id, T underwritingRequest) {
        this.id = id;
        this.underwritingRequest = underwritingRequest;
        this.flow = StrategyFlow.of(underwritingRequest.getType());
        this.strategyContext = StrategyContext.of(underwritingRequest);
        this.flowStatus = UnderwritingFlowStatusEnum.CREATED;
        this.strategyContext.setCurrentStrategy(getCurrentStrategy().getStrategyName());
    }

    public void execute() {
        StrategyContext<T> strategyContext = getCurrentStrategy().execute(getStrategyContext());
        setStrategyContext(strategyContext);
    }

    public StrategyResult getStrategyResult() {
        return strategyContext.getStrategyResult();
    }

    public boolean hasNextStrategy() {
        return getCurrentStrategy().getNextStrategy() != null;
    }

    public final Strategy<T> getCurrentStrategy() {
        return strategyContext.getCurrentStrategy() == null ? flow.getFirstStrategy()
                : flow.getStrategy(strategyContext.getCurrentStrategy());
    }

}
