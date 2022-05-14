package com.shopee.demo.infrastructure.utils;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils implements ApplicationContextAware {
    public static ObjectMapper mapper;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        mapper = context.getBean(ObjectMapper.class);
    }

    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return mapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return mapper.readTree(content);
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readTree(URL source) {
        try {
            return mapper.readTree(source);
        } catch (IOException e) {
            // TODO
            throw new RuntimeException(e);
        }
    }
}
