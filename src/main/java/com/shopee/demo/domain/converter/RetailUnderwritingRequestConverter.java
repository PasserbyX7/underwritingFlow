package com.shopee.demo.domain.converter;

import com.shopee.demo.domain.type.request.RetailUnderwritingRequest;
import com.shopee.demo.infrastructure.data.RetailUnderwritingDO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RetailUnderwritingRequestConverter {
    RetailUnderwritingRequestConverter INSTANCE = Mappers.getMapper(RetailUnderwritingRequestConverter.class);

    RetailUnderwritingRequest convert(RetailUnderwritingDO retailUnderwritingRequestDO);

    RetailUnderwritingDO convert(RetailUnderwritingRequest retailUnderwritingRequest);
}
