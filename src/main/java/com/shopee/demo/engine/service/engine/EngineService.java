package com.shopee.demo.engine.service.engine;

import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.engine.EngineOutput;

public interface EngineService {
    EngineOutput execute(EngineInput engineInput);
}
