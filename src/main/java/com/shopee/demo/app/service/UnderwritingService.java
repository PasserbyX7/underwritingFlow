package com.shopee.demo.app.service;

import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface UnderwritingService {

    void executeUnderwriting(UnderwritingRequest underwritingRequest);

    void maintainUnderwritingFlow();
}
