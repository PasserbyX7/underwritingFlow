package com.shopee.demo.engine.factory;

import javax.annotation.Resource;

import com.shopee.demo.engine.repository.converter.RetailUnderwritingRequestConverter;
import com.shopee.demo.engine.repository.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;
import com.shopee.demo.infrastructure.dal.dao.RetailUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;

import org.springframework.stereotype.Component;

@Component
public class UnderwritingRequestFactory {

    @Resource
    private SmeUnderwritingDAO smeUnderwritingDAO;

    @Resource
    private RetailUnderwritingDAO retailUnderwritingDAO;

    public UnderwritingRequest create(UnderwritingTypeEnum underwritingType, String underwritingId) {
        switch (underwritingType) {
            case SME:
                return SmeUnderwritingRequestConverter.INSTANCE
                        .convert(smeUnderwritingDAO.selectByRequestId(underwritingId));
            case RETAIL:
                return RetailUnderwritingRequestConverter.INSTANCE
                        .convert(retailUnderwritingDAO.selectByRequestId(underwritingId));
            default:
                throw new IllegalArgumentException();
        }
    }

}
