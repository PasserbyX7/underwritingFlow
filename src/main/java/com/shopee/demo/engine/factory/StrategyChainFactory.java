package com.shopee.demo.engine.factory;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyChain;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class StrategyChainFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @SuppressWarnings("unchecked")
    public static <T extends UnderwritingRequest> StrategyChain<T> getStrategyChain(UnderwritingTypeEnum type) {
        String strategyChainName = type.toString().toLowerCase() + "StrategyChain";
        try {
            return applicationContext.getBean(strategyChainName, StrategyChain.class);
        } catch (BeansException e) {
            throw new IllegalArgumentException("undefined underwriting strategy chain " + strategyChainName);
        }
    }

}
