package com.shopee.demo.engine.entity.machine;

import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import static com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum.*;

import java.util.EnumSet;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowStateMachineTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowStateMachineTest {

    @Mock
    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine;

    @Test
    @Timeout(3)
    void testExecute() throws Exception {
        // given
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine = mockMachine();
        // then
        FlowStateMachine.of(machine).start();
        FlowStateMachine.of(machine).execute();
        FlowStateMachine.of(machine).stop();
    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> mockMachine() throws Exception {
        StateMachineBuilder.Builder<UnderwritingFlowStatusEnum, FlowEventEnum> builder = StateMachineBuilder.builder();
        builder.configureStates().withStates().initial(INITIAL).states(EnumSet.allOf(UnderwritingFlowStatusEnum.class));
        builder.configureTransitions().withExternal().source(INITIAL).target(APPROVED).event(FlowEventEnum.START);
        return builder.build();
    }

}
