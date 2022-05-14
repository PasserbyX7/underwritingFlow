package com.shopee.demo.engine.repository.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.engine.EngineOutput;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyContainer;
import com.shopee.demo.engine.type.strategy.StrategyResult;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;
import com.shopee.demo.infrastructure.utils.jackson.JsonUtils;

public class UnderwritingFlowConverter {

    public static UnderwritingFlowDO convert(UnderwritingFlow flow) {
        UnderwritingFlowDO flowDO = new UnderwritingFlowDO();
        flowDO.setId(flow.getId());
        flowDO.setUnderwritingId(flow.getUnderwritingRequest().getUnderwritingId());
        flowDO.setUnderwritingType(flow.getUnderwritingRequest().getUnderwritingType());
        flowDO.setFlowStatus(flow.getFlowStatus());
        flowDO.setCurrentStrategy(flow.getCurrentStrategyName());
        flowDO.setStrategyInput(flow.getStrategyContext().getStrategyInput().toJson());
        flowDO.setStrategyOutput(flow.getStrategyContext().getStrategyOutput().toJson());
        if (flow.getStrategyContext().getStrategyResult() != null) {
            flowDO.setStrategyStatus(flow.getStrategyContext().getStrategyResult().getStatus());
            flowDO.setSuspendDataSource(flow.getStrategyContext().getStrategyResult().getSuspendDataSource());
            flowDO.setErrorMsg(flow.getStrategyContext().getStrategyResult().getErrorMsg());
        }
        return flowDO;
    }

    public static UnderwritingFlow convert(UnderwritingFlowDO flowDO, UnderwritingRequest request) {
        StrategyContext<UnderwritingRequest> strategyContext = new StrategyContext<>(request,
                JsonUtils.readValue(flowDO.getStrategyInput(), new TypeReference<StrategyContainer<EngineInput>>() {
                }),
                JsonUtils.readValue(flowDO.getStrategyInput(), new TypeReference<StrategyContainer<EngineOutput>>() {
                }));
        StrategyResult StrategyResult = new StrategyResult(flowDO.getStrategyStatus(), flowDO.getSuspendDataSource(),
                flowDO.getErrorMsg(), flowDO.getRejectedStrategy());
        strategyContext.setStrategyResult(StrategyResult);
        UnderwritingFlow flow = new UnderwritingFlow(flowDO.getId(), request, strategyContext,
                flowDO.getFlowStatus(), flowDO.getCurrentStrategy());
        return flow;
    }

}
