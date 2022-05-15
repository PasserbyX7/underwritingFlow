package com.shopee.demo.infrastructure.dal.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUnderwritingDO extends BaseDO {
    public String underwritingId;
    public Long requestTime;
    public Long requestExpireTime;
}
