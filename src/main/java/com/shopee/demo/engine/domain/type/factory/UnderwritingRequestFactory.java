package com.shopee.demo.engine.domain.type.factory;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.domain.converter.RetailUnderwritingRequestConverter;
import com.shopee.demo.engine.domain.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dao.RetailUnderwritingDAO;
import com.shopee.demo.infrastructure.dao.SmeUnderwritingDAO;

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
