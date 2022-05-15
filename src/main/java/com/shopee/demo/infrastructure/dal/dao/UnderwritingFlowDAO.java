package com.shopee.demo.infrastructure.dal.dao;

import java.util.Optional;

import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

public interface UnderwritingFlowDAO {
    int insertSelective(UnderwritingFlowDO underwritingFlowDO);

    int updateByPrimaryKeySelective(UnderwritingFlowDO underwritingFlowDO);

    Optional<UnderwritingFlowDO> selectByPrimaryKey(Long id);
}
