package com.shopee.demo.engine.repository;

import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;

public interface UnderwritingRequestRepository {

    void save(UnderwritingRequest underwritingRequest);

    UnderwritingRequest find(String underwritingId, UnderwritingTypeEnum underwritingType);
}
