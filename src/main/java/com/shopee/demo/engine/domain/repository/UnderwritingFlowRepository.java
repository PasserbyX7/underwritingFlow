package com.shopee.demo.engine.domain.repository;

import com.shopee.demo.engine.domain.entity.UnderwritingFlow;

public interface UnderwritingFlowRepository {

    long save(UnderwritingFlow<?> underwritingFlow);

    UnderwritingFlow<?> load(long underwritingFlowId);

}
