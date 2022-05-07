package com.shopee.demo.infrastructure.dal.dao.impl;

import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

import org.springframework.stereotype.Service;

@Service
public class SmeUnderwritingDAOImpl implements SmeUnderwritingDAO {

    @Override
    public SmeUnderwritingDO selectByUnderwritingId(String underwritingId) {
        SmeUnderwritingDO smeUnderwritingDO = new SmeUnderwritingDO();
        smeUnderwritingDO.setUnderwritingId("underwritingId");
        return smeUnderwritingDO;
    }

    @Override
    public void insertSelective(SmeUnderwritingDO smeUnderwritingDO) {
        // TODO Auto-generated method stub

    }

}
