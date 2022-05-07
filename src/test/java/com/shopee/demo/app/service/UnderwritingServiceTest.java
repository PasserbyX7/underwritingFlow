package com.shopee.demo.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;

import com.shopee.demo.app.service.impl.UnderwritingServiceImpl;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.strategy.Strategy;
import com.shopee.demo.engine.factory.StrategyChainFactory;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.service.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.type.request.SmeUnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;
import com.shopee.demo.engine.type.strategy.AbstractStrategyChain;
import com.shopee.demo.engine.type.strategy.StrategyChain;
import com.shopee.demo.engine.type.strategy.sme.SmeStrategy1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingServiceTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingServiceTest {

    @InjectMocks
    private UnderwritingServiceImpl underwritingService;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private TransactionStatus transactionStatus;

    @Mock
    private UnderwritingRequestRepository requestRepository;

    @Mock
    private UnderwritingFlowRepository flowRepository;

    @Mock
    private UnderwritingFlowExecuteService flowExecuteService;

    @Test
    void testExecuteUnderwritingSuccess() {
        // given
        UnderwritingRequest underwritingRequest = mockSmeUnderwritingRequest();
        StrategyChain<SmeUnderwritingRequest> smeStrategyChain=mockSmeStrategyChain();
        // when
        doAnswer(invocation -> invocation.<TransactionCallback<Long>>getArgument(0).doInTransaction(transactionStatus))
                .when(transactionTemplate)
                .execute(any());
        doReturn(1L)
                .when(flowRepository)
                .save(any(UnderwritingFlow.class));
        mockStatic(StrategyChainFactory.class)
                .when(() -> StrategyChainFactory.create(eq(UnderwritingTypeEnum.SME)))
                .thenReturn(smeStrategyChain);
        // then
        underwritingService.executeUnderwriting(underwritingRequest);
    }



    private UnderwritingRequest mockSmeUnderwritingRequest() {
        return new UnderwritingRequest() {

            @Override
            public UnderwritingTypeEnum getUnderwritingType() {
                return UnderwritingTypeEnum.SME;
            }

            @Override
            public String getUnderwritingId() {
                return "mockedUnderwritingId";
            }

            @Override
            public Long getRequestTime() {
                return 1651922495900L;
            }

            @Override
            public Long getRequestExpireTime() {
                return 1651922495900L;
            }

        };
    }

    private StrategyChain<SmeUnderwritingRequest> mockSmeStrategyChain() {
        return new AbstractStrategyChain<SmeUnderwritingRequest>() {

            @Override
            public Strategy<SmeUnderwritingRequest> getFirstStrategy() {
                // TODO Auto-generated method stub
                return new SmeStrategy1();
            }

            @Override
            protected void configStrategyChain() {
                // TODO Auto-generated method stub

            }

        };
    }

}
