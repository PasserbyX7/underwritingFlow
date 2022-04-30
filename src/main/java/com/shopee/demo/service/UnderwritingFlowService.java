package com.shopee.demo.service;

import com.shopee.demo.domain.UnderwritingRequest;

public interface UnderwritingFlowService {
    /**
     *
     * @param underwritingRequest
     * @return underwritingContextId
     */
    long createUnderwritingTask(UnderwritingRequest underwritingDO);

    void executeUnderwritingTask(long underwritingContextId);
}
