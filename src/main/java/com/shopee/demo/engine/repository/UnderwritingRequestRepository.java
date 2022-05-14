package com.shopee.demo.engine.repository;

import java.util.Optional;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

public interface UnderwritingRequestRepository {

    void save(UnderwritingRequest underwritingRequest);

    Optional<UnderwritingRequest> find(String underwritingId, UnderwritingTypeEnum underwritingType);
}
