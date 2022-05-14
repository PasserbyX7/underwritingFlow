package com.shopee.demo.engine.service.engine;

import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.strategy.StrategyInput;

/**
 * StrategyInput字段填充器
 *
 * @author li.cao
 * @since 2022-04-21
 */
public interface EngineInputFieldFiller {

    /**
     * 用source对象字段填充target对象字段
     *
     * @param source business input DTO
     * @param target StrategyInput
     */
    void fill(StrategyInput strategyInput, EngineInput engineInput);

}
