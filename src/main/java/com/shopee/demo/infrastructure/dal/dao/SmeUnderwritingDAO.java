package com.shopee.demo.infrastructure.dal.dao;

import java.util.Optional;

import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

public interface SmeUnderwritingDAO {

    Optional<SmeUnderwritingDO> selectByUnderwritingId(String underwritingId);

    void insertSelective(SmeUnderwritingDO smeUnderwritingDO);

}
