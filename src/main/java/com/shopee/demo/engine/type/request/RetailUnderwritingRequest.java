package com.shopee.demo.engine.type.request;

import lombok.Value;

@Value
public class RetailUnderwritingRequest implements UnderwritingRequest {

    private String underwritingId;
    private Long requestTime;
    private Long requestExpireTime;
    private String smeData;

    @Override
    public UnderwritingTypeEnum getUnderwritingType() {
        return UnderwritingTypeEnum.RETAIL;
    }

}
