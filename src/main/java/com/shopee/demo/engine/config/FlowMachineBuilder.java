package com.shopee.demo.engine.config;

import java.util.EnumSet;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import static com.shopee.demo.engine.constant.FlowEventEnum.*;
import static com.shopee.demo.engine.constant.FlowStatusEnum.*;
import static com.shopee.demo.engine.constant.StrategyStatusEnum.*;

@Component
@EnableStateMachine
public class FlowMachineBuilder {

    public static final String FLOW_STATE_MACHINE_ID = "UnderwritingFlowMachine";

    private BeanFactory beanFactory;

    private FlowStateMachinePersistService flowStateMachinePersistService;

    public FlowMachineBuilder(BeanFactory beanFactory, FlowStateMachinePersistService flowStateMachinePersistService) {
        this.beanFactory = beanFactory;
        this.flowStateMachinePersistService = flowStateMachinePersistService;
    }

    public StateMachine<FlowStatusEnum, FlowEventEnum> build() throws Exception {
        StateMachineBuilder.Builder<FlowStatusEnum, FlowEventEnum> builder = StateMachineBuilder.builder();
        configure(builder.configureConfiguration());
        configure(builder.configureStates());
        configure(builder.configureTransitions());
        return builder.build();
    }

    private void configure(StateMachineStateConfigurer<FlowStatusEnum, FlowEventEnum> states)
            throws Exception {
        states.withStates()
                .initial(INITIAL)
                .choice(CHOICE)
                .stateDo(ONGOING, executeStrategyAction())
                .states(EnumSet.allOf(FlowStatusEnum.class));
    }

    private void configure(StateMachineTransitionConfigurer<FlowStatusEnum, FlowEventEnum> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(INITIAL).target(ONGOING).event(START)
                .and()
                .withExternal()
                .source(PENDING).target(ONGOING).event(START)
                .and()
                .withExternal()
                .source(ONGOING).target(CHOICE).event(START).action(executeStrategyAction())
                .and()
                .withExternal()
                .source(ONGOING).target(CHOICE).event(STRATEGY_EXECUTE)
                .and()
                .withChoice()
                .source(CHOICE)
                .first(REJECTED, rejectGuard())
                .then(CANCELLED, cancelledGuard())
                .then(EXPIRED, expiredGuard())
                .then(PENDING, pendingGuard())
                .then(APPROVED, approvedGuard())
                .last(ONGOING, setNextStrategyAction());
    }

    private void configure(StateMachineConfigurationConfigurer<FlowStatusEnum, FlowEventEnum> config)
            throws Exception {
        config.withConfiguration()
                .beanFactory(beanFactory)
                .listener(underwritingFlowPersisterListener())
                .machineId(FLOW_STATE_MACHINE_ID);
    }

    @Bean
    public Guard<FlowStatusEnum, FlowEventEnum> approvedGuard() {
        return new Guard<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow underwritingFlow = UnderwritingFlow.from(context.getExtendedState());
                StrategyStatusEnum strategyStatus = underwritingFlow.getStrategyResultStatus();
                return strategyStatus == PASS && !underwritingFlow.hasNextStrategy();
            }

        };
    }

    @Bean
    public Guard<FlowStatusEnum, FlowEventEnum> rejectGuard() {
        return new Guard<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                return UnderwritingFlow.from(context.getExtendedState()).getStrategyResultStatus() == REJECT;
            }

        };
    }

    @Bean
    public Guard<FlowStatusEnum, FlowEventEnum> cancelledGuard() {
        return new Guard<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                return UnderwritingFlow.from(context.getExtendedState()).getStrategyResultStatus() == ERROR;
            }

        };
    }

    @Bean
    public Guard<FlowStatusEnum, FlowEventEnum> expiredGuard() {
        return new Guard<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                return UnderwritingFlow.from(context.getExtendedState()).getStrategyResultStatus() == EXPIRE;
            }

        };
    }

    @Bean
    public Guard<FlowStatusEnum, FlowEventEnum> pendingGuard() {
        return new Guard<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                return UnderwritingFlow.from(context.getExtendedState()).getStrategyResultStatus() == SUSPEND;
            }

        };
    }

    @Bean
    public Action<FlowStatusEnum, FlowEventEnum> executeStrategyAction() {
        return new Action<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public void execute(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow.from(context.getExtendedState()).execute();
                context.getStateMachine()
                        .sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.STRATEGY_EXECUTE).build()))
                        .blockFirst();
            }

        };
    }

    @Bean
    public Action<FlowStatusEnum, FlowEventEnum> setNextStrategyAction() {
        return new Action<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public void execute(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow.from(context.getExtendedState()).setNextStrategy();
            }

        };
    }

    @Bean
    public StateMachineListener<FlowStatusEnum, FlowEventEnum> underwritingFlowPersisterListener() {
        return new StateMachineListenerAdapter<FlowStatusEnum, FlowEventEnum>() {
            @Override
            public void stateContext(StateContext<FlowStatusEnum, FlowEventEnum> stateContext) {
                if (stateContext.getStage() == Stage.STATE_ENTRY) {
                    UnderwritingFlow.from(stateContext.getExtendedState())
                            .setFlowStatus(stateContext.getStateMachine().getState().getId());
                    try {
                        flowStateMachinePersistService.persist(stateContext.getStateMachine());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                        // TODO
                    }
                }
            }

            @Override
            public void stateMachineError(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine,
                    Exception exception) {
                exception.printStackTrace();
            }
        };
    }

}