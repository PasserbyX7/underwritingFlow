package com.shopee.demo.infrastructure.dal.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RetailUnderwritingDO extends BaseUnderwritingDO {
    private String retailData;
}
