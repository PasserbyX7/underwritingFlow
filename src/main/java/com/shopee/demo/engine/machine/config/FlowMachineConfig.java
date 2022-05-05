package com.shopee.demo.engine.machine.config;

import com.shopee.demo.engine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.MachineId;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.entity.UnderwritingFlow;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import static com.shopee.demo.engine.constant.FlowEventEnum.*;
import static com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum.*;

import java.util.EnumSet;

import javax.annotation.Resource;

@Configuration
@EnableStateMachineFactory(contextEvents = false)
public class FlowMachineConfig extends EnumStateMachineConfigurerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum> {

    @Resource
    private Action<UnderwritingFlowStatusEnum, FlowEventEnum> executeStrategyAction;

    @Resource
    private Action<UnderwritingFlowStatusEnum, FlowEventEnum> setNextStrategyAction;

    @Resource
    private Action<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineTerminalAction;

    @Override
    public void configure(StateMachineStateConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> states)
            throws Exception {
        states.withStates()
                .initial(CREATED)
                .choice(CHOICE)
                .state(ONGOING, executeStrategyAction)
                .state(APPROVED, stateMachineTerminalAction)
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
                .last(ONGOING, setNextStrategyAction)
                .and()
                .withExternal()
                .source(APPROVED).target(END).event(FLOW_EXIT);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> config)
            throws Exception {
        config.withConfiguration()
                .machineId(MachineId.UNDERWRITING_FLOW_ID);
    }

    private Guard<UnderwritingFlowStatusEnum, FlowEventEnum> approvedGuard() {
        return new Guard<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(
                        ExtendedStateEnum.UNDERWRITING_CONTEXT,
                        UnderwritingFlow.class);
                StrategyStatusEnum strategyStatus = underwritingContext.getStrategyResult().getStatus();
                return strategyStatus == StrategyStatusEnum.PASS && !underwritingContext.hasNextStrategy();
            }

        };
    }

    private Guard<UnderwritingFlowStatusEnum, FlowEventEnum> rejectGuard() {

        return new Guard<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                // return false;
                UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(
                        ExtendedStateEnum.UNDERWRITING_CONTEXT,
                        UnderwritingFlow.class);
                return underwritingContext.getStrategyResult().getStatus() == StrategyStatusEnum.REJECT;
            }

        };
    }

}
