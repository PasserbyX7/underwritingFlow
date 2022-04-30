package com.shopee.demo.domain;

import com.shopee.demo.constant.UnderwritingTypeEnum;

import lombok.Data;

@Data
public abstract class UnderwritingRequest  {
    abstract public UnderwritingTypeEnum getType();
    protected String underwritingId;
    protected Long requestTime;
    protected Long requestExpireTime;
}
