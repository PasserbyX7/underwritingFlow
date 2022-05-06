package com.shopee.demo.engine.repository.converter;

import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SmeUnderwritingRequestConverter {
    SmeUnderwritingRequestConverter INSTANCE = Mappers.getMapper(SmeUnderwritingRequestConverter.class);

    SmeUnderwritingRequest convert(SmeUnderwritingDO SmeUnderwritingRequestDO);

    SmeUnderwritingDO convert(SmeUnderwritingRequest smeUnderwritingRequest);
}
