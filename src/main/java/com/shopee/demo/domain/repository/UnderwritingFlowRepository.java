package com.shopee.demo.domain.repository;

import com.shopee.demo.domain.entity.UnderwritingFlow;

public interface UnderwritingFlowRepository {

    long save(UnderwritingFlow<?> underwritingFlow);

    UnderwritingFlow<?> load(long underwritingFlowId);

}
