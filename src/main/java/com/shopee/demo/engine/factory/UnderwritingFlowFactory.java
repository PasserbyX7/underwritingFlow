package com.shopee.demo.engine.factory;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.converter.StrategyContainerConverter;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.springframework.stereotype.Component;

@Component
public class UnderwritingFlowFactory {

    public UnderwritingFlow<UnderwritingRequest> create(UnderwritingRequest underwritingRequest) {
        Long id = null;
        UnderwritingFlowStatusEnum flowStatus = UnderwritingFlowStatusEnum.CREATED;
        StrategyContext<UnderwritingRequest> strategyContext = StrategyContext.of(underwritingRequest);
        return new UnderwritingFlow<UnderwritingRequest>(id, underwritingRequest, strategyContext, flowStatus, null);
    }

    public UnderwritingFlow<UnderwritingRequest> create(UnderwritingFlowDO flowDO) {

        UnderwritingRequest request = null;
        // requestRepository.find(flowDO.getUnderwritingId(),
        // flowDO.getUnderwritingType());

        StrategyContext<UnderwritingRequest> strategyContext = new StrategyContext<>(request,
                StrategyContainerConverter.convert(flowDO.getStrategyInput()),
                StrategyContainerConverter.convert(flowDO.getStrategyOutput()));
        UnderwritingFlow<UnderwritingRequest> flow = new UnderwritingFlow<>(flowDO.getId(), request, strategyContext,
                flowDO.getFlowStatus(), flowDO.getCurrentStrategy());
        return flow;
    }
}
