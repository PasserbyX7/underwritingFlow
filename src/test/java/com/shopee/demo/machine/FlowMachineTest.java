package com.shopee.demo.machine;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.repository.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.engine.service.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.config.StateMachineFactory;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
public class FlowMachineTest {

    @Resource
    StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> factory;

    @Resource
    UnderwritingFlowExecuteService underwritingFlowService;

    @Resource
    UnderwritingFlowRepository underwritingContextService;

    @MockBean
    UnderwritingFlowDAO underwritingContextDAO;

    @Test
    void test() throws Exception {
        // given
        Map<Long, UnderwritingFlowDO> underwritingContextMap = new HashMap<>();
        Iterator<Long> iter = Stream.iterate(0L, e -> e + 1).iterator();
        UnderwritingFlow<?> flow = UnderwritingFlow.of(SmeUnderwritingRequestConverter.INSTANCE
                .convert(new SmeUnderwritingDO()));
        // when
        doAnswer(invocation -> {
            UnderwritingFlowDO entity = invocation.getArgument(0);
            if (entity.getId() == null) {
                entity.setId(iter.next());
            }
            underwritingContextMap.put(entity.getId(), entity);
            return entity;
        })
                .when(underwritingContextDAO)
                .saveOrUpdateById(any(UnderwritingFlowDO.class));
        doAnswer(invocation -> underwritingContextMap.get(invocation.getArgument(0)))
                .when(underwritingContextDAO)
                .selectByPrimaryKey(anyLong());
        // then
        long ctxId = underwritingContextService.save(flow);
        log.info("*************状态机测试开始*************");
        underwritingFlowService.executeUnderwritingFlowAsync(ctxId);
        log.info("*************状态机测试结束*************");
    }

}
