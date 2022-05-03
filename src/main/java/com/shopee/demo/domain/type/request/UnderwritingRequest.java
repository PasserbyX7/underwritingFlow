package com.shopee.demo.domain.type.request;

import com.shopee.demo.constant.UnderwritingTypeEnum;


public interface UnderwritingRequest  {
    UnderwritingTypeEnum getType();
    String getUnderwritingId();
    Long getRequestTime();
    Long getRequestExpireTime();
}
