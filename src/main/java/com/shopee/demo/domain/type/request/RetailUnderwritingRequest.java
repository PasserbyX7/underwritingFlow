package com.shopee.demo.domain.type.request;

import com.shopee.demo.constant.UnderwritingTypeEnum;

import lombok.Value;

@Value
public class RetailUnderwritingRequest implements UnderwritingRequest {

    private String underwritingId;
    private Long requestTime;
    private Long requestExpireTime;
    private String smeData;

    @Override
    public UnderwritingTypeEnum getType() {
        return UnderwritingTypeEnum.RETAIL;
    }

}
