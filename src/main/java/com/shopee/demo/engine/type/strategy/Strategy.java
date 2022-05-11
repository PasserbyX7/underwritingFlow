package com.shopee.demo.engine.type.strategy;

import java.util.List;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.strategy.StrategyContext;
import com.shopee.demo.engine.factory.EngineInputFactory;
import com.shopee.demo.engine.service.engine.EngineService;
import com.shopee.demo.engine.service.intergration.DataIntegration;
import com.shopee.demo.engine.type.engine.EngineInput;
import com.shopee.demo.engine.type.engine.EngineOutput;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public abstract class Strategy<T extends UnderwritingRequest> {
    public abstract StrategyEnum getStrategyName();

    @Resource
    private EngineService engineService;

    @Resource
    private EngineInputFactory engineInputFactory;

    @Setter
    @Getter
    protected Strategy<T> nextStrategy;

    /**
     * 模板方法
     */
    public StrategyResult execute(StrategyContext<T> strategyContext) {
        UnderwritingTypeEnum underwritingType = strategyContext.getUnderwritingRequest().getUnderwritingType();
        EngineInput engineInput = engineInputFactory.createSmeEngineInput(underwritingType);
        for (DataIntegration<T> dataIntegration : getDataIntegrations()) {
            dataIntegration.integration(strategyContext, engineInput);
        }
        EngineOutput engineOutput = engineService.execute(engineInput);
        strategyContext.getStrategyOutput().put(getStrategyName(), engineOutput);
        return StrategyResult.pass();
    }

    public boolean hasNextStrategy() {
        return nextStrategy != null;
    }

    abstract protected List<DataIntegration<T>> getDataIntegrations();
}
