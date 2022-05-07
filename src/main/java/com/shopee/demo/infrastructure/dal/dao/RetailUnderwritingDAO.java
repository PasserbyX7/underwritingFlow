package com.shopee.demo.infrastructure.dal.dao;

import com.shopee.demo.infrastructure.dal.data.RetailUnderwritingDO;

public interface RetailUnderwritingDAO {
    RetailUnderwritingDO selectByUnderwritingId(String UnderwritingId);
}
