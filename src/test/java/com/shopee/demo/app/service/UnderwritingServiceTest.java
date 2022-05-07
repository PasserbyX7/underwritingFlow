package com.shopee.demo.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.shopee.demo.app.service.impl.UnderwritingServiceImpl;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;

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

    @Test
    void testExecuteUnderwritingSuccess() {
        // given
        UnderwritingRequest underwritingRequest = mockSmeUnderwritingRequest();
        // when
        doAnswer(invocation -> invocation.<TransactionCallback<Long>>getArgument(0).doInTransaction(transactionStatus))
                .when(transactionTemplate).execute(any());
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

}
