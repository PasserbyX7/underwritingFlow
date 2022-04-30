package com.shopee.demo.machine;

import javax.annotation.Resource;

import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.context.UnderwritingFlow;
import com.shopee.demo.dao.UnderwritingContextDAO;
import com.shopee.demo.domain.SmeUnderwritingRequest;
import com.shopee.demo.entity.SmeUnderwritingDO;
import com.shopee.demo.entity.UnderwritingContextDO;
import com.shopee.demo.service.UnderwritingContextService;
import com.shopee.demo.service.UnderwritingFlowService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

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
    UnderwritingFlowService underwritingFlowService;

    @Resource
    UnderwritingContextService underwritingContextService;

    @Resource
    StateMachinePersister<UnderwritingFlowStatusEnum, FlowEventEnum, Long> persister;

    @MockBean
    UnderwritingContextDAO underwritingContextDAO;

    @Test
    void test() throws Exception {
        // given
        Map<Long, UnderwritingContextDO> underwritingContextMap = new HashMap<>();
        Iterator<Long> iter = Stream.iterate(0L, e -> e + 1).iterator();
        // UnderwritingRequest request = underwritingRequestFactory.create(UnderwritingTypeEnum.SME, "underwritingId");
        UnderwritingFlow<?> flow = underwritingContextService.create(new SmeUnderwritingRequest(new SmeUnderwritingDO()));
        // when
        doAnswer(invocation -> {
            UnderwritingContextDO entity = invocation.getArgument(0);
            if (entity.getId() == null) {
                entity.setId(iter.next());
            }
            underwritingContextMap.put(entity.getId(), entity);
            return entity;
        })
                .when(underwritingContextDAO)
                .saveOrUpdateById(any(UnderwritingContextDO.class));
        doAnswer(invocation -> underwritingContextMap.get(invocation.getArgument(0)))
                .when(underwritingContextDAO)
                .selectByPrimaryKey(anyLong());
        // then
        long ctxId = underwritingContextService.save(flow);
        log.info("*************状态机测试开始*************");
        underwritingFlowService.executeUnderwritingTask(ctxId);
        log.info("*************状态机测试结束*************");
    }

}
