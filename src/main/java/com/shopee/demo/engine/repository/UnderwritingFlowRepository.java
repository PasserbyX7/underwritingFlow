package com.shopee.demo.engine.repository;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;

public interface UnderwritingFlowRepository {

    long save(UnderwritingFlow<?> underwritingFlow);

    UnderwritingFlow<?> load(long underwritingFlowId);

}
