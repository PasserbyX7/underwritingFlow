package com.shopee.demo.engine.type.request;

import com.shopee.demo.engine.repository.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

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

    public static SmeUnderwritingRequest of(SmeUnderwritingDO requestDO){
        return SmeUnderwritingRequestConverter.INSTANCE.convert(requestDO);
    }

}
