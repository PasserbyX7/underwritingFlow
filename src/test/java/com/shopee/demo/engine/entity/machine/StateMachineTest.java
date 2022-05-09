package com.shopee.demo.engine.entity.machine;

import com.google.common.collect.ImmutableMap;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.config.FlowMachineBuilder;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.machine.ExtendedStateEnum;
import com.shopee.demo.engine.type.request.UnderwritingRequest;
import com.shopee.demo.engine.type.request.UnderwritingTypeEnum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static com.shopee.demo.engine.type.flow.FlowEventEnum.*;
import static com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StateMachineTest")
@MockitoSettings(strictness = Strictness.LENIENT)
public class StateMachineTest {

    private FlowMachineBuilder flowMachineBuilder = new FlowMachineBuilder(null);

    @Test
    void test() throws Exception {
        // given
        UnderwritingFlow underwritingFlow = mockUnderwritingFlow();
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine = mockMachine(underwritingFlow);
        // then
        StateMachineTestPlanBuilder
                .<UnderwritingFlowStatusEnum, FlowEventEnum>builder()
                .stateMachine(machine)
                .step()
                .expectStates(INITIAL)
                .and()
                .step()
                .sendEvent(START)
                .expectStateChanged(1)
                .expectStates(ONGOING)
                .expectVariable(ExtendedStateEnum.UNDERWRITING_CONTEXT)
                .and()
                .build()
                .test();
    }

    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> mockMachine(UnderwritingFlow underwritingFlow)
            throws Exception {
        StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine = flowMachineBuilder.build();
        StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> context = new DefaultStateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum>(
                underwritingFlow.getFlowStatus(),
                null,
                null,
                new DefaultExtendedState(ImmutableMap.of(ExtendedStateEnum.UNDERWRITING_CONTEXT, underwritingFlow)),
                null,
                FlowMachineBuilder.FLOW_STATE_MACHINE_ID);
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(function -> function.resetStateMachineReactively(context).block());
        return stateMachine;
    }

    private UnderwritingFlow mockUnderwritingFlow() {
        return UnderwritingFlow.of(new UnderwritingRequest() {

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
        });
    }
}
