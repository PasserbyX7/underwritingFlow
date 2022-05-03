package com.shopee.demo.service;

import com.shopee.demo.domain.entity.UnderwritingFlow;
import com.shopee.demo.domain.type.request.UnderwritingRequest;

public interface UnderwritingContextService {

    long save(UnderwritingFlow<?> context);

    UnderwritingFlow<?> load(long underwritingContextId);

    UnderwritingFlow<?> create(UnderwritingRequest underwritingDO);

}
