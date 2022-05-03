package com.shopee.demo.context;

import java.util.concurrent.CountDownLatch;

import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.domain.UnderwritingRequest;
import com.shopee.demo.flow.StrategyFlow;
import com.shopee.demo.strategy.Strategy;
import com.shopee.demo.strategy.StrategyResult;

import lombok.Data;

@Data
public final class UnderwritingFlow<T extends UnderwritingRequest> {

    private final Long id;
    private final String underwritingId;
    private final UnderwritingTypeEnum underwritingType;
    private final Long requestTime;
    private final Long requestExpireTime;
    private final StrategyFlow<T> flow;
    private StrategyContext<T> strategyContext;
    private UnderwritingFlowStatusEnum flowStatus;

    private final CountDownLatch latch = new CountDownLatch(1);

    public UnderwritingFlow(Long id, T underwritingDO) {
        this.id = id;
        this.underwritingId = underwritingDO.getUnderwritingId();
        this.underwritingType = underwritingDO.getType();
        this.requestTime = underwritingDO.getRequestTime();
        this.requestExpireTime = underwritingDO.getRequestExpireTime();
        this.flow = StrategyFlow.of(underwritingDO.getType());
        this.strategyContext = StrategyContext.of(underwritingDO);
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
