package com.shopee.demo.engine.factory;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.demo.engine.type.engine.EngineOutput;

import org.springframework.stereotype.Component;

/**
 * @author li.cao
 * @since 2022-04-24
 */
@Component
public class EngineOutputFactory {

    @Resource
    private ObjectMapper mapper;

    public EngineOutput create(String content) {
        try {
            return EngineOutput.of(mapper.readTree(content));
        } catch (JsonProcessingException e) {
            //TODO
            throw new RuntimeException();
        }
    }

}
