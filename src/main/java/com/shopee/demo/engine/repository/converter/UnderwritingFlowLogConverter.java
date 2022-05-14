package com.shopee.demo.engine.repository.converter;

import com.shopee.demo.engine.entity.flow.UnderwritingFlowLog;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowLogDO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UnderwritingFlowLogConverter {
    UnderwritingFlowLogConverter INSTANCE = Mappers.getMapper(UnderwritingFlowLogConverter.class);

    UnderwritingFlowLogDO convert(UnderwritingFlowLog underwritingFlowLog);

    @Mappings({
            @Mapping(source = "id", target = "underwritingFlowId")
    })
    UnderwritingFlowLog convert(UnderwritingFlowDO underwritingFlowDO);
}
