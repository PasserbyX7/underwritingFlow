package com.shopee.demo.infrastructure.dal.data;

import com.shopee.demo.engine.repository.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmeUnderwritingDO extends BaseUnderwritingDO {
    private String smeData;

    public static SmeUnderwritingDO of(SmeUnderwritingRequest request){
        return SmeUnderwritingRequestConverter.INSTANCE.convert(request);
    }

}
