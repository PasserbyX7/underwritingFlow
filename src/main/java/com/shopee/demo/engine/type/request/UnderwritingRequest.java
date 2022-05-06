package com.shopee.demo.engine.type.request;

public interface UnderwritingRequest  {
    UnderwritingTypeEnum getUnderwritingType();
    String getUnderwritingId();
    Long getRequestTime();
    Long getRequestExpireTime();
}
