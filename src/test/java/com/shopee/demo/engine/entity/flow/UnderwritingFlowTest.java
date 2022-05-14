package com.shopee.demo.engine.entity.flow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.strategy.Strategy;
import com.shopee.demo.engine.type.strategy.StrategyChain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingFlowTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingFlowTest {

    @Mock
    private UnderwritingRequest underwritingRequest;

    @Mock
    private StrategyChain<UnderwritingRequest> strategyChain;

    @Mock
    private Strategy<UnderwritingRequest> strategy;

    @Test
    void tesExecute() {
        // given
        UnderwritingFlow flow = UnderwritingFlow.of(underwritingRequest);
        doReturn(strategyChain).when(underwritingRequest).getStrategyChain();
        doReturn(strategy).when(strategyChain).getFirstStrategy();
        // when
        flow.execute();
        // then
        verify(strategy, times(1)).execute(any());
    }

}
