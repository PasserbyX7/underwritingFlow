package com.shopee.demo.application.service;

import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface UnderwritingService {
    void executeUnderwriting(UnderwritingRequest underwritingRequest);
    void maintainUnderwritingFlow();
    void createUnderwritingFlow(long underwritingId);
}
