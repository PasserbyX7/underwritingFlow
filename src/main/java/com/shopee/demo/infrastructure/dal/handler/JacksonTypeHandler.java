package com.shopee.demo.infrastructure.dal.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Jackson 实现 JSON 字段类型处理器
 *
 * @author li.cao
 * @since: 2021-11-23 18:29:29
 */
@NoArgsConstructor
@MappedTypes({ Object.class })
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JacksonTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static ObjectMapper OBJECT_MAPPER;
    private Class<?> type;

    public JacksonTypeHandler(Class<?> type) {
        this.type = type;
    }

    public static ObjectMapper getObjectMapper() {
        if (null == OBJECT_MAPPER)
            OBJECT_MAPPER = new ObjectMapper();
        return OBJECT_MAPPER;
    }

    @Override
    protected Object parse(String json) {
        try {
            if (getTypeReference() == null)
                return getObjectMapper().readValue(json, type);
            else
                return getObjectMapper().readValue(json, getTypeReference());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected TypeReference<?> getTypeReference() {
        return null;
    }
}
