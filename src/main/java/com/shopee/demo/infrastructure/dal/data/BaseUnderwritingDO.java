package com.shopee.demo.infrastructure.dal.data;

import lombok.Data;

@Data
public class BaseUnderwritingDO {
    public String underwritingId;
    public Long requestTime;
    public Long requestExpireTime;
}
