package com.shopee.demo.engine.repository.impl;

import java.util.Optional;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

import org.springframework.stereotype.Repository;

@Repository
public class UnderwritingRequestRepositoryImpl implements UnderwritingRequestRepository {

    @Resource
    private SmeUnderwritingDAO smeUnderwritingDAO;

    @Override
    public void save(UnderwritingRequest underwritingRequest) {
        switch (underwritingRequest.getUnderwritingType()) {
            case SME:
                smeUnderwritingDAO.insertSelective(SmeUnderwritingDO.of((SmeUnderwritingRequest) underwritingRequest));
                break;
            default:
                throw new IllegalArgumentException("undefined underwriting type");
        }

    }

    @Override
    public Optional<UnderwritingRequest> find(String underwritingId, UnderwritingTypeEnum underwritingType) {
        switch (underwritingType) {
            case SME:
                return smeUnderwritingDAO.selectByUnderwritingId(underwritingId).map(SmeUnderwritingRequest::of);
            default:
                throw new IllegalArgumentException("undefined underwriting type");
        }
    }
}
