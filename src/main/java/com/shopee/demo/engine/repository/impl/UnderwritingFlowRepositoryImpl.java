package com.shopee.demo.engine.repository.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.flow.UnderwritingFlowLog;
import com.shopee.demo.engine.exception.flow.FlowException;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.repository.converter.UnderwritingFlowConverter;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowLogDAO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class UnderwritingFlowRepositoryImpl implements UnderwritingFlowRepository {

    @Resource
    private UnderwritingFlowDAO underwritingFlowDAO;

    @Resource
    private UnderwritingFlowLogDAO underwritingFlowLogDAO;

    @Resource
    private UnderwritingRequestRepository requestRepository;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public long save(UnderwritingFlow flow) {
        return transactionTemplate.execute(e -> {
            UnderwritingFlowDO flowDO = UnderwritingFlowConverter.convert(flow);
            saveOrUpdateById(flowDO);
            UnderwritingFlowLog flowLog = UnderwritingFlowLog.of(flowDO);
            underwritingFlowLogDAO.insertSelective(flowLog.convertToDO());
            return flowDO.getId();
        });
    }

    @Override
    public UnderwritingFlow find(long underwritingFlowId) {
        UnderwritingFlowDO flowDO = underwritingFlowDAO
                .selectByPrimaryKey(underwritingFlowId)
                .orElseThrow(() -> new FlowException("Underwriting flow does not exist"));
        UnderwritingRequest request = requestRepository
                .find(flowDO.getUnderwritingId(), flowDO.getUnderwritingType())
                .orElseThrow(() -> new FlowException("Underwriting request does not exist"));
        UnderwritingFlow flow = UnderwritingFlowConverter.convert(flowDO, request);
        return flow;
    }

    private void saveOrUpdateById(UnderwritingFlowDO flowDO) {
        if (flowDO.getId() == null) {
            underwritingFlowDAO.insertSelective(flowDO);
        } else {
            underwritingFlowDAO.updateByPrimaryKeySelective(flowDO);
        }
    }
}
