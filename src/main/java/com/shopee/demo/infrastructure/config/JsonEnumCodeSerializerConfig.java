package com.shopee.demo.infrastructure.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shopee.demo.infrastructure.constant.CommonEnum;

/**
 * @author li.cao
 * @since 2022-04-22
 */
public class JsonEnumCodeSerializerConfig extends JsonSerializer<CommonEnum> {

    @Override
    public void serialize(CommonEnum value, JsonGenerator gen, SerializerProvider serializer) throws IOException {
        serializer.defaultSerializeValue(value.getCode(), gen);
    }

}