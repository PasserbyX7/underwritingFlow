package com.shopee.demo.machine;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.engine.service.flow.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.infrastructure.dal.dao.SmeUnderwritingDAO;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowLogDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
public class FlowMachineTest {

    @Resource
    UnderwritingFlowExecuteService underwritingFlowService;

    @Resource
    UnderwritingFlowRepository underwritingFlowRepository;

    @MockBean
    UnderwritingFlowDAO underwritingFlowDAO;

    @MockBean
    SmeUnderwritingDAO smeUnderwritingDAO;

    @MockBean
    UnderwritingFlowLogDAO underwritingFlowLogDAO;

    @MockBean
    TransactionTemplate transactionTemplate;

    @Test
    void test() throws Exception {
        // given
        Map<Long, UnderwritingFlowDO> underwritingContextMap = new HashMap<>();
        Iterator<Long> iter = Stream.iterate(0L, e -> e + 1).iterator();
        UnderwritingFlow flow = mockUnderwritingFlow();
        // when
        doAnswer(inv -> inv.<TransactionCallback<Long>>getArgument(0).doInTransaction(new SimpleTransactionStatus()))
                .when(transactionTemplate)
                .execute(any());
        doReturn(Optional.ofNullable(mockSmeUnderwritingDO()))
                .when(smeUnderwritingDAO)
                .selectByUnderwritingId(any());
        doAnswer(inv -> {
            UnderwritingFlowDO entity = inv.getArgument(0);
            entity.setId(iter.next());
            underwritingContextMap.put(entity.getId(), entity);
            return 1;
        }).when(underwritingFlowDAO)
                .insertSelective(any());
        doAnswer(inv -> {
            UnderwritingFlowDO entity = inv.getArgument(0);
            entity.setId(iter.next());
            underwritingContextMap.put(entity.getId(), entity);
            return 1;
        }).when(underwritingFlowDAO)
                .updateByPrimaryKeySelective(any());
        doAnswer(invocation -> Optional.ofNullable(underwritingContextMap.get(invocation.getArgument(0))))
                .when(underwritingFlowDAO)
                .selectByPrimaryKey(anyLong());
        // then
        long ctxId = underwritingFlowRepository.save(flow);
        log.info("*************授信异步调用开始*************");
        underwritingFlowService.executeUnderwritingFlowAsync(ctxId);
        log.info("*************授信异步调用结束*************");
        TimeUnit.SECONDS.sleep(3);
    }

    private UnderwritingFlow mockUnderwritingFlow() {
        SmeUnderwritingDO smeUnderwritingDO = new SmeUnderwritingDO();
        UnderwritingRequest smeUnderwritingRequest = SmeUnderwritingRequestConverter.INSTANCE
                .convert(smeUnderwritingDO);
        return UnderwritingFlow.of(smeUnderwritingRequest);
    }

    private SmeUnderwritingDO mockSmeUnderwritingDO() {
        SmeUnderwritingDO smeUnderwritingDO = new SmeUnderwritingDO();
        smeUnderwritingDO.setUnderwritingId("underwritingId");
        return smeUnderwritingDO;
    }
}
