package com.shopee.demo.engine.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.shopee.demo.engine.constant.UnderwritingTypeEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.flow.UnderwritingFlowLog;
import com.shopee.demo.engine.repository.converter.UnderwritingFlowConverter;
import com.shopee.demo.engine.repository.impl.UnderwritingFlowRepositoryImpl;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowLogDAO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnderwritingFlowRepositoryTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnderwritingFlowRepositoryTest {

    @InjectMocks
    private UnderwritingFlowRepositoryImpl underwritingFlowRepository;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private UnderwritingFlow flow;

    @Mock
    private UnderwritingFlowDO flowDO;

    @Mock
    private UnderwritingRequest request;

    @Mock
    private UnderwritingFlowDAO underwritingFlowDAO;

    @Mock
    private UnderwritingFlowLogDAO underwritingFlowLogDAO;

    @Mock
    private UnderwritingRequestRepository requestRepository;

    @Mock
    private UnderwritingFlowLog flowLog;

    private static MockedStatic<UnderwritingFlowConverter> underwritingFlowConverter;

    private static MockedStatic<UnderwritingFlowLog> underwritingFlowLog;

    @BeforeAll
    static public void beforeAll() {
        underwritingFlowConverter = mockStatic(UnderwritingFlowConverter.class);
        underwritingFlowLog = mockStatic(UnderwritingFlowLog.class);
    }

    @AfterAll
    public static void afterAll() {
        underwritingFlowConverter.close();
        underwritingFlowLog.close();
    }

    @Test
    void testSave() {
        // given
        doAnswer(inv -> inv.<TransactionCallback<Long>>getArgument(0).doInTransaction(new SimpleTransactionStatus()))
                .when(transactionTemplate)
                .execute(any());
        underwritingFlowConverter
                .when(() -> UnderwritingFlowConverter.convert(any()))
                .thenReturn(flowDO);
        underwritingFlowLog.when(() -> UnderwritingFlowLog.of(any()))
                .thenReturn(flowLog);
        // when
        underwritingFlowRepository.save(flow);
        // then
        verify(underwritingFlowDAO, times(1)).saveOrUpdateById(any());
        verify(underwritingFlowLogDAO, times(1)).insertSelective(any());
    }

    @Test
    void testFind() {
        // given
        long flowId = 1L;
        doReturn("id").when(flowDO).getUnderwritingId();
        doReturn(UnderwritingTypeEnum.SME).when(flowDO).getUnderwritingType();
        doReturn(Optional.ofNullable(flowDO)).when(underwritingFlowDAO).selectByPrimaryKey(anyLong());
        doReturn(Optional.ofNullable(request)).when(requestRepository).find(anyString(), any());
        underwritingFlowConverter
                .when(() -> UnderwritingFlowConverter.convert(any(), any()))
                .thenReturn(flow);
        // when
        underwritingFlowRepository.find(flowId);
        // then
        verify(underwritingFlowDAO, times(1)).selectByPrimaryKey(anyLong());
        verify(requestRepository, times(1)).find(anyString(), any());
    }

}
