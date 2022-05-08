package com.shopee.demo.engine.service;

import com.shopee.demo.engine.service.impl.FlowStateMachineServiceImpl;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowStateMachineServiceTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowStateMachineServiceTest {

    @InjectMocks
    private FlowStateMachineServiceImpl flowStateMachineService;

    @Mock
    private StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineFactory;

    @Mock
    private StateMachinePersist<UnderwritingFlowStatusEnum, FlowEventEnum, Long> stateMachinePersist;

    @Test
    void testAcquire() {

    }

    @Test
    void testExecute() {

    }

    @Test
    void testRelease() {

    }

}
