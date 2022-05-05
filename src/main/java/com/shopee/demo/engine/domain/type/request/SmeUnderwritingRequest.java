package com.shopee.demo.engine.domain.type.request;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;

import lombok.Value;

@Value
public class SmeUnderwritingRequest implements UnderwritingRequest {

    private String underwritingId;
    private Long requestTime;
    private Long requestExpireTime;
    private String smeData;

    @Override
    public UnderwritingTypeEnum getUnderwritingType() {
        return UnderwritingTypeEnum.SME;
    }

}
