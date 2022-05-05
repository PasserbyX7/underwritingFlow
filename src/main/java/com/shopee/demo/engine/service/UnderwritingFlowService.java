package com.shopee.demo.engine.service;

import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;

public interface UnderwritingFlowService {
    /**
     *
     * @param underwritingRequest
     * @return underwritingFlowId
     */
    long createUnderwritingTask(UnderwritingRequest underwritingDO);

    void executeUnderwritingTask(long underwritingContextId);
}
