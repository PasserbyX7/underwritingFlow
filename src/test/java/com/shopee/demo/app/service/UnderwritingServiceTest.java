package com.shopee.demo.app.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.shopee.demo.app.service.impl.UnderwritingServiceImpl;
import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.exception.flow.FlowException;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.UnderwritingRequestRepository;
import com.shopee.demo.engine.service.flow.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.type.request.UnderwritingRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.support.SimpleTransactionStatus;
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
    private UnderwritingRequestRepository requestRepository;

    @Mock
    private UnderwritingFlowRepository flowRepository;

    @Mock
    private UnderwritingFlowExecuteService flowExecuteService;

    @Test
    void testExecuteUnderwritingSuccess() {
        // given
        UnderwritingRequest underwritingRequest = mockSmeUnderwritingRequest();
        doAnswer(inv -> inv.<TransactionCallback<Long>>getArgument(0).doInTransaction(new SimpleTransactionStatus()))
                .when(transactionTemplate)
                .execute(any());
        doReturn(1L)
                .when(flowRepository)
                .save(any(UnderwritingFlow.class));
        // when
        underwritingService.executeUnderwriting(underwritingRequest);
        // then
        verify(requestRepository, times(1)).save(any(UnderwritingRequest.class));
        verify(flowRepository, times(1)).save(any(UnderwritingFlow.class));
        verify(flowExecuteService, times(1)).executeUnderwritingFlowAsync(anyLong());
    }

    @Test
    void testExecuteUnderwritingFail() {
        // given
        UnderwritingRequest underwritingRequest = mockSmeUnderwritingRequest();
        doReturn(Optional.of(underwritingRequest)).when(requestRepository).find(anyString(), any());
        // when
        assertThrows(FlowException.class, () -> underwritingService.executeUnderwriting(underwritingRequest));
        // then
        verify(requestRepository, never()).save(any(UnderwritingRequest.class));
        verify(flowRepository, never()).save(any(UnderwritingFlow.class));
        verify(flowExecuteService, never()).executeUnderwritingFlowAsync(anyLong());
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
            public boolean isExpire() {
                return false;
            }

        };
    }

}
