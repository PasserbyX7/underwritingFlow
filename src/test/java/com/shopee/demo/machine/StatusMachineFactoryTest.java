package com.shopee.demo.machine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.MachineId;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.converter.SmeUnderwritingRequestConverter;
import com.shopee.demo.engine.domain.entity.UnderwritingFlow;
import com.shopee.demo.engine.domain.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;
import com.shopee.demo.infrastructure.dal.dao.UnderwritingFlowDAO;
import com.shopee.demo.infrastructure.dal.data.SmeUnderwritingDO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

@SpringBootTest
public class StatusMachineFactoryTest {

    @Resource
    private StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineFactory;

    @Resource
    private FlowStateMachineService flowStateMachineService;

    @MockBean
    private UnderwritingFlowDAO underwritingContextDAO;

    @Resource
    private UnderwritingFlowRepository underwritingContextService;

    @Test
    void test() {
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
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m1 = stateMachineFactory.getStateMachine("test");
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m2 = stateMachineFactory
                .getStateMachine(MachineId.UNDERWRITING_FLOW_ID);
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m3 = stateMachineFactory
                .getStateMachine(MachineId.UNDERWRITING_FLOW_ID);
        long ctxId1 = underwritingContextService.save(flow);
        long ctxId2 = underwritingContextService.save(flow);
        long ctxId3 = underwritingContextService.save(flow);
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m4 = flowStateMachineService.acquireStateMachine(ctxId1);
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m5 = flowStateMachineService.acquireStateMachine(ctxId2);
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> m6 = flowStateMachineService.acquireStateMachine(ctxId3);
        System.out.println(m4);
        System.out.println(m5);
        System.out.println(m6);
    }
}
