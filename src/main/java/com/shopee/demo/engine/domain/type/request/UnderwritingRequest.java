package com.shopee.demo.engine.domain.type.request;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;


public interface UnderwritingRequest  {
    UnderwritingTypeEnum getUnderwritingType();
    String getUnderwritingId();
    Long getRequestTime();
    Long getRequestExpireTime();
}
