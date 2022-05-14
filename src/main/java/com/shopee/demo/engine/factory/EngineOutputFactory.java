package com.shopee.demo.engine.factory;

import com.shopee.demo.engine.type.engine.EngineOutput;
import com.shopee.demo.infrastructure.utils.JsonUtils;

/**
 * @author li.cao
 * @since 2022-04-24
 */

public class EngineOutputFactory {

    public EngineOutput create(String content) {
        return EngineOutput.of(JsonUtils.readTree(content));
    }

}
