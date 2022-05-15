package com.shopee.demo.engine.factory;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.StrategyChain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import static com.shopee.demo.engine.constant.UnderwritingTypeEnum.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StrategyChainFactoryTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class StrategyChainFactoryTest {

    @InjectMocks
    private StrategyChainFactory strategyChainFactory;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private StrategyChain<UnderwritingRequest> strategyChain;

    @BeforeEach
    public void beforeEach() {
        new StrategyChainFactory().setApplicationContext(applicationContext);
    }

    @Test
    void testGetSmeStrategyChain() {
        // given
        doReturn(strategyChain).when(applicationContext).getBean(eq("smeStrategyChain"), eq(StrategyChain.class));
        // then
        StrategyChainFactory.getStrategyChain(SME);
    }

    @Test
    void testGetUndefinedStrategyChain() {
        // given
        doThrow(new NoSuchBeanDefinitionException(""))
                .when(applicationContext)
                .getBean(anyString(), eq(StrategyChain.class));
        // then
        assertThrows(IllegalArgumentException.class, () -> StrategyChainFactory.getStrategyChain(RETAIL));
    }

}
