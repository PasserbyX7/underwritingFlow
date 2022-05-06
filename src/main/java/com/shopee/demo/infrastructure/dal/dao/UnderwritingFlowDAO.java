package com.shopee.demo.infrastructure.dal.dao;

import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

public interface UnderwritingFlowDAO {
    int insertSelective(UnderwritingFlowDO underwritingContextDO);
    UnderwritingFlowDO selectByPrimaryKey(Long id);

    void saveOrUpdateById(UnderwritingFlowDO underwritingContextDO);
}
