package com.shopee.demo.dao;

import com.shopee.demo.entity.SmeUnderwritingDO;

import org.springframework.stereotype.Repository;

@Repository
public class SmeUnderwritingDAO {
    public SmeUnderwritingDO selectByRequestId(String requestId) {
        SmeUnderwritingDO smeUnderwritingDO = new SmeUnderwritingDO();
        smeUnderwritingDO.setUnderwritingId(requestId);
        smeUnderwritingDO.setRequestTime(1L);
        smeUnderwritingDO.setRequestExpireTime(2L);
        return smeUnderwritingDO;
    }
}
