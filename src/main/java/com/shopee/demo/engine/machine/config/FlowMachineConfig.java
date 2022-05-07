package com.shopee.demo.engine.machine.config;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.machine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.machine.constant.MachineId;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.strategy.StrategyStatusEnum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import reactor.core.publisher.Mono;

import static com.shopee.demo.engine.type.flow.FlowEventEnum.*;
import static com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum.*;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(contextEvents = false)
public class FlowMachineConfig extends EnumStateMachineConfigurerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {

    @Override
    public void configure(StateMachineStateConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> states)
            throws Exception {
        states.withStates()
                .initial(CREATED)
                .choice(CHOICE)
                .state(ONGOING, executeStrategyAction())
                .states(EnumSet.allOf(UnderwritingFlowStatusEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(CREATED).target(ONGOING).event(START)
                .and()
                .withExternal()
                .source(ONGOING).target(CHOICE).event(STRATEGY_EXECUTE)
                .and()
                .withChoice()
                .source(CHOICE)
                .first(REJECT, rejectGuard())
                .then(APPROVED, approvedGuard())
                .last(ONGOING, setNextStrategyAction());
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> config)
            throws Exception {
        config.withConfiguration()
                .machineId(MachineId.UNDERWRITING_FLOW_ID);
    }

    @Bean
    public Guard<UnderwritingFlowStatusEnum, FlowEventEnum> approvedGuard() {
        return new Guard<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(
                        ExtendedStateEnum.UNDERWRITING_CONTEXT,
                        UnderwritingFlow.class);
                StrategyStatusEnum strategyStatus = underwritingContext.getStrategyResultStatus();
                return strategyStatus == StrategyStatusEnum.PASS && !underwritingContext.hasNextStrategy();
            }

        };
    }

    @Bean
    public Guard<UnderwritingFlowStatusEnum, FlowEventEnum> rejectGuard() {
        return new Guard<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                // return false;
                UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(
                        ExtendedStateEnum.UNDERWRITING_CONTEXT,
                        UnderwritingFlow.class);
                return underwritingContext.getStrategyResultStatus() == StrategyStatusEnum.REJECT;
            }

        };
    }

    @Bean
    public Action<UnderwritingFlowStatusEnum, FlowEventEnum> executeStrategyAction() {
        return new Action<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                context.getExtendedState()
                        .get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class)
                        .execute();
                context.getStateMachine()
                        .sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.STRATEGY_EXECUTE).build()))
                        .subscribe();
            }

        };
    }

    @Bean
    public Action<UnderwritingFlowStatusEnum, FlowEventEnum> setNextStrategyAction() {
        return new Action<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                context.getExtendedState()
                        .get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class)
                        .setNextStrategy();
            }

        };
    }
}
