package com.shopee.demo.engine.repository.impl;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.repository.converter.UnderwritingFlowConverter;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyContainer;
import com.shopee.demo.engine.type.strategy.input.StrategyInput;
import com.shopee.demo.engine.type.strategy.output.StrategyOutput;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UnderwritingFlowRepositoryImpl implements UnderwritingFlowRepository {

    @Resource
    private UnderwritingRequestRepository requestRepository;

    @Resource
    private UnderwritingFlowDAO underwritingFlowDAO;

    @Resource
    private ObjectMapper mapper;

    @Override
    public long save(UnderwritingFlow<?> flow) {
        UnderwritingFlowDO flowDO=UnderwritingFlowConverter.convert(flow);
        log.info("保存授信Flow：当前策略[{}] 当前状态[{}]", flow.getCurrentStrategyName(), flow.getFlowStatus());
        underwritingFlowDAO.saveOrUpdateById(flowDO);
        return flowDO.getId();
    }

    @Override
    public UnderwritingFlow<?> find(long underwritingFlowId) {
        UnderwritingFlowDO flowDO = underwritingFlowDAO.selectByPrimaryKey(underwritingFlowId);
        UnderwritingRequest request = requestRepository.find(flowDO.getUnderwritingId(), flowDO.getUnderwritingType());
        StrategyContainer<StrategyInput> strategyInput = null;
        StrategyContainer<StrategyOutput> strategyOutput = null;
        try {
            strategyInput = mapper.readValue(flowDO.getStrategyInput(),
                    new TypeReference<StrategyContainer<StrategyInput>>() {
                    });
            strategyOutput = mapper.readValue(flowDO.getStrategyInput(),
                    new TypeReference<StrategyContainer<StrategyOutput>>() {
                    });
        } catch (Exception e) {
            // TODO
        }
        StrategyContext<UnderwritingRequest> strategyContext = new StrategyContext<>(request, strategyInput,
                strategyOutput);
        UnderwritingFlow<UnderwritingRequest> flow = new UnderwritingFlow<>(flowDO.getId(), request, strategyContext,
                flowDO.getFlowStatus(), flowDO.getCurrentStrategy());
        log.info("加载授信Flow：当前策略[{}] 当前状态[{}]", flow.getCurrentStrategyName(), flow.getFlowStatus());
        return flow;
    }

}
