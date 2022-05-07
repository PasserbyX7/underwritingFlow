package com.shopee.demo.infrastructure.dal.dao;

import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

public interface SmeUnderwritingDAO {

    SmeUnderwritingDO selectByUnderwritingId(String underwritingId);

    void insertSelective(SmeUnderwritingDO smeUnderwritingDO);

}
