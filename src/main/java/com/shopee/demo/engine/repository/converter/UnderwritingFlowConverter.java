package com.shopee.demo.engine.repository.converter;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.type.converter.StrategyContainerConverter;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.springframework.stereotype.Component;


public class UnderwritingFlowConverter {



    public static <T extends UnderwritingRequest>UnderwritingFlow<T> convert(UnderwritingFlowDO flowDO) {
        return null;
        // UnderwritingFlowDO flowDO = underwritingFlowDAO.selectByPrimaryKey(underwritingFlowId);
        // UnderwritingRequest request = requestRepository.find(flowDO.getUnderwritingId(), flowDO.getUnderwritingType());
        // StrategyContainer<StrategyInput> strategyInput = null;
        // StrategyContainer<StrategyOutput> strategyOutput = null;
        // try {
        //     strategyInput = mapper.readValue(flowDO.getStrategyInput(),
        //             new TypeReference<StrategyContainer<StrategyInput>>() {
        //             });
        //     strategyOutput = mapper.readValue(flowDO.getStrategyInput(),
        //             new TypeReference<StrategyContainer<StrategyOutput>>() {
        //             });
        // } catch (Exception e) {
        //     // TODO
        // }
        // StrategyContext<UnderwritingRequest> strategyContext = new StrategyContext<>(request, strategyInput,
        //         strategyOutput);
        // UnderwritingFlow<UnderwritingRequest> flow = new UnderwritingFlow<>(flowDO.getId(), request, strategyContext,
        //         flowDO.getFlowStatus(), flowDO.getCurrentStrategy());
        // log.info("加载授信Flow：当前策略[{}] 当前状态[{}]", flow.getCurrentStrategyName(), flow.getFlowStatus());
        // return flow;
    }

    public static <T extends UnderwritingRequest> UnderwritingFlowDO convert(UnderwritingFlow<T> flow) {
        UnderwritingFlowDO flowDO = new UnderwritingFlowDO();
        flowDO.setUnderwritingId(flow.getUnderwritingRequest().getUnderwritingId());
        flowDO.setUnderwritingType(flow.getUnderwritingRequest().getUnderwritingType());
        flowDO.setFlowStatus(flow.getFlowStatus());
        flowDO.setCurrentStrategy(flow.getCurrentStrategyName());
        flowDO.setStrategyStatus(flow.getStrategyContext().getStrategyResult().getStatus());
        flowDO.setSuspendDataSource(flow.getStrategyContext().getStrategyResult().getSuspendDataSource());
        flowDO.setTerminalReason(flow.getStrategyContext().getStrategyResult().getTerminalReason());
        flowDO.setStrategyInput(StrategyContainerConverter.convert(flow.getStrategyContext().getStrategyInput()));
        flowDO.setStrategyOutput(StrategyContainerConverter.convert(flow.getStrategyContext().getStrategyOutput()));
        return flowDO;
    }
}
