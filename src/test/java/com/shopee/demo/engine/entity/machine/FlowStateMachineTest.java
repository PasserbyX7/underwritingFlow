package com.shopee.demo.engine.entity.machine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import static com.shopee.demo.engine.constant.FlowStatusEnum.*;

import java.util.EnumSet;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowStateMachineTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class FlowStateMachineTest {

    @Test
    @Timeout(3)
    void testExecute() throws Exception {
        // given
        StateMachine<FlowStatusEnum, FlowEventEnum> machine = mockMachine();
        // then
        FlowStateMachine.of(machine).start();
        FlowStateMachine.of(machine).execute();
        FlowStateMachine.of(machine).stop();
    }

    private StateMachine<FlowStatusEnum, FlowEventEnum> mockMachine() throws Exception {
        StateMachineBuilder.Builder<FlowStatusEnum, FlowEventEnum> builder = StateMachineBuilder.builder();
        builder.configureStates().withStates().initial(INITIAL).states(EnumSet.allOf(FlowStatusEnum.class));
        builder.configureTransitions().withExternal().source(INITIAL).target(PENDING).event(FlowEventEnum.START);
        return builder.build();
    }

}
