package com.shopee.demo.engine.domain.type.strategy;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;

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
    public static <T extends UnderwritingRequest> StrategyChain<T> create(UnderwritingTypeEnum type) {
        String strategyChainName = type.toString().toLowerCase() + "StrategyFlow";
        return applicationContext.getBean(strategyChainName, StrategyChain.class);
    }

}
