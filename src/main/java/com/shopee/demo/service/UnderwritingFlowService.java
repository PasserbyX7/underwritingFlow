package com.shopee.demo.service;

import com.shopee.demo.domain.type.request.UnderwritingRequest;

public interface UnderwritingFlowService {
    /**
     *
     * @param underwritingRequest
     * @return underwritingContextId
     */
    long createUnderwritingTask(UnderwritingRequest underwritingDO);

    void executeUnderwritingTask(long underwritingContextId);
}
