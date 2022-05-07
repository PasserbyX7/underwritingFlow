package com.shopee.demo.engine.repository.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.type.request.RetailUnderwritingRequest;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;
import com.shopee.demo.infrastructure.dal.dao.RetailUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

import org.springframework.stereotype.Repository;

@Repository
public class UnderwritingRequestRepositoryImpl implements UnderwritingRequestRepository {

    @Resource
    private SmeUnderwritingDAO smeUnderwritingDAO;

    @Resource
    private RetailUnderwritingDAO retailUnderwritingDAO;

    @Override
    public void save(UnderwritingRequest underwritingRequest) {
        switch (underwritingRequest.getUnderwritingType()) {
            case SME:
                smeUnderwritingDAO.insertSelective(SmeUnderwritingDO.of((SmeUnderwritingRequest) underwritingRequest));
                break;
            case RETAIL:
                break;
            default:
                throw new IllegalArgumentException();
        }

    }

    @Override
    public UnderwritingRequest find(String underwritingId, UnderwritingTypeEnum underwritingType) {
        switch (underwritingType) {
            case SME:
                return SmeUnderwritingRequest.of(smeUnderwritingDAO.selectByUnderwritingId(underwritingId));
            case RETAIL:
                return RetailUnderwritingRequest.of(retailUnderwritingDAO.selectByUnderwritingId(underwritingId));
            default:
                throw new IllegalArgumentException();
        }
    }
}
