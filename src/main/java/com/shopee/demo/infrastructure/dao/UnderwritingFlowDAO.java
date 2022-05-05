package com.shopee.demo.infrastructure.dao;

import com.shopee.demo.infrastructure.data.UnderwritingFlowDO;

public interface UnderwritingFlowDAO {
    int insertSelective(UnderwritingFlowDO underwritingContextDO);
    UnderwritingFlowDO selectByPrimaryKey(Long id);

    void saveOrUpdateById(UnderwritingFlowDO underwritingContextDO);
}
