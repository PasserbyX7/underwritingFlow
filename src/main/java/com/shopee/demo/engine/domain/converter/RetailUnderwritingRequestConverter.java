package com.shopee.demo.engine.domain.converter;

import com.shopee.demo.engine.domain.type.request.RetailUnderwritingRequest;
import com.shopee.demo.infrastructure.dal.data.RetailUnderwritingDO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RetailUnderwritingRequestConverter {
    RetailUnderwritingRequestConverter INSTANCE = Mappers.getMapper(RetailUnderwritingRequestConverter.class);

    RetailUnderwritingRequest convert(RetailUnderwritingDO retailUnderwritingRequestDO);

    RetailUnderwritingDO convert(RetailUnderwritingRequest retailUnderwritingRequest);
}
