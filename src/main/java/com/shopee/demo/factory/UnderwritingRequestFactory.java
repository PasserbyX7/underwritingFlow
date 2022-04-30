package com.shopee.demo.factory;

import javax.annotation.Resource;

import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.dao.RetailUnderwritingDAO;
import com.shopee.demo.dao.SmeUnderwritingDAO;
import com.shopee.demo.domain.SmeUnderwritingRequest;
import com.shopee.demo.domain.UnderwritingRequest;

import org.springframework.stereotype.Component;

@Component
public class UnderwritingRequestFactory {

    @Resource
    private SmeUnderwritingDAO smeUnderwritingDAO;

    @Resource
    private RetailUnderwritingDAO retailUnderwritingDAO;

    public UnderwritingRequest create(UnderwritingTypeEnum type, String underwritingId) {
        switch (type) {
            case SME:
                return new SmeUnderwritingRequest(smeUnderwritingDAO.selectByRequestId(underwritingId));
            // case RETAIL:
            // return retailUnderwritingDAO.selectByRequestId(underwritingId);
            default:
                throw new IllegalArgumentException();
        }
    }

}
