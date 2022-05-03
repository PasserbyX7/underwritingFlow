package com.shopee.demo.infrastructure.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmeUnderwritingDO extends BaseUnderwritingDO {
    private String smeData;
}
