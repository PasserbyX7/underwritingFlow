package com.shopee.demo.domain;

import com.shopee.demo.constant.UnderwritingTypeEnum;
import com.shopee.demo.entity.SmeUnderwritingDO;

public class SmeUnderwritingRequest extends UnderwritingRequest {

    public SmeUnderwritingRequest(SmeUnderwritingDO underwritingDO){
        this.underwritingId=underwritingDO.getUnderwritingId();
    }

    @Override
    public UnderwritingTypeEnum getType() {
        return UnderwritingTypeEnum.SME;
    }


}
