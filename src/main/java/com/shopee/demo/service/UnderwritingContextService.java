package com.shopee.demo.service;

import com.shopee.demo.context.UnderwritingFlow;
import com.shopee.demo.domain.UnderwritingRequest;

public interface UnderwritingContextService {

    long save(UnderwritingFlow<?> context);

    UnderwritingFlow<?> load(long underwritingContextId);

    UnderwritingFlow<?> create(UnderwritingRequest underwritingDO);

}
