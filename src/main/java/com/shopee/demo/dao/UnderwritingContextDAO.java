package com.shopee.demo.dao;

import com.shopee.demo.entity.UnderwritingContextDO;

public interface UnderwritingContextDAO {
    int insertSelective(UnderwritingContextDO underwritingContextDO);
    UnderwritingContextDO selectByPrimaryKey(Long id);

    void saveOrUpdateById(UnderwritingContextDO underwritingContextDO);
}
