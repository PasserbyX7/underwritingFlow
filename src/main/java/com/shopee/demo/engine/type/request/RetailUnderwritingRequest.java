package com.shopee.demo.engine.type.request;

import com.shopee.demo.engine.repository.converter.RetailUnderwritingRequestConverter;
import com.shopee.demo.infrastructure.dal.data.RetailUnderwritingDO;

import lombok.Value;

@Value
public class RetailUnderwritingRequest implements UnderwritingRequest {

    private String underwritingId;
    private Long requestTime;
    private Long requestExpireTime;
    private String retailData;

    @Override
    public UnderwritingTypeEnum getUnderwritingType() {
        return UnderwritingTypeEnum.RETAIL;
    }

    @Override
    public boolean isExpire() {
        return requestTime >= requestExpireTime;
    }

    public static RetailUnderwritingRequest of(RetailUnderwritingDO requestDO) {
        return RetailUnderwritingRequestConverter.INSTANCE.convert(requestDO);
    }

}
