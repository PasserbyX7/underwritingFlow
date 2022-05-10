package com.shopee.demo.engine.repository.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.repository.converter.UnderwritingFlowConverter;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UnderwritingFlowRepositoryImpl implements UnderwritingFlowRepository {

    @Resource
    private UnderwritingFlowDAO underwritingFlowDAO;

    @Resource
    private UnderwritingRequestRepository requestRepository;

    @Override
    public long save(UnderwritingFlow flow) {
        UnderwritingFlowDO flowDO = UnderwritingFlowConverter.convert(flow);
        log.info("保存授信Flow：当前状态[{}] 当前策略[{}]",flow.getFlowStatus(), flow.getCurrentStrategyName());
        underwritingFlowDAO.saveOrUpdateById(flowDO);
        return flowDO.getId();
    }

    @Override
    public UnderwritingFlow find(long underwritingFlowId) {
        UnderwritingFlowDO flowDO = underwritingFlowDAO.selectByPrimaryKey(underwritingFlowId);
        UnderwritingRequest request = requestRepository.find(flowDO.getUnderwritingId(), flowDO.getUnderwritingType());
        UnderwritingFlow flow = UnderwritingFlowConverter.convert(flowDO, request);
        log.info("加载授信Flow：当前策略[{}] 当前状态[{}]", flow.getCurrentStrategyName(), flow.getFlowStatus());
        return flow;
    }

}
