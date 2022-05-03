package com.shopee.demo.domain.converter;

import com.shopee.demo.domain.type.request.SmeUnderwritingRequest;
import com.shopee.demo.infrastructure.data.SmeUnderwritingDO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SmeUnderwritingRequestConverter {
    SmeUnderwritingRequestConverter INSTANCE = Mappers.getMapper(SmeUnderwritingRequestConverter.class);

    SmeUnderwritingRequest convert(SmeUnderwritingDO SmeUnderwritingRequestDO);

    SmeUnderwritingDO convert(SmeUnderwritingRequest smeUnderwritingRequest);
}
