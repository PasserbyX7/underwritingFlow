package com.shopee.demo.engine.service;

import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface UnderwritingFlowService {
    /**
     *
     * @param underwritingRequest
     * @return underwritingFlowId
     */
    long createUnderwritingTask(UnderwritingRequest underwritingDO);

    void executeUnderwritingTaskAsync(long underwritingContextId);
}
