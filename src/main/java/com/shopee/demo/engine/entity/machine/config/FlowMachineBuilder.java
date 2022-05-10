package com.shopee.demo.engine.entity.machine.config;

import java.util.EnumSet;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.strategy.StrategyStatusEnum;

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

import static com.shopee.demo.engine.type.flow.FlowEventEnum.*;
import static com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum.*;

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

    public StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> build() throws Exception {
        StateMachineBuilder.Builder<UnderwritingFlowStatusEnum, FlowEventEnum> builder = StateMachineBuilder.builder();
        configure(builder.configureConfiguration());
        configure(builder.configureStates());
        configure(builder.configureTransitions());
        return builder.build();
    }

    private void configure(StateMachineStateConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> states)
            throws Exception {
        states.withStates()
                .initial(INITIAL)
                .choice(CHOICE)
                .stateDo(ONGOING, executeStrategyAction())
                .states(EnumSet.allOf(UnderwritingFlowStatusEnum.class));
    }

    private void configure(StateMachineTransitionConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(INITIAL).target(ONGOING).event(START)
                .and()
                .withExternal()
                .source(ONGOING).target(CHOICE).event(STRATEGY_EXECUTE)
                .and()
                .withChoice()
                .source(CHOICE)
                .first(REJECTED, rejectGuard())
                .then(APPROVED, approvedGuard())
                .last(ONGOING, setNextStrategyAction());
    }

    private void configure(StateMachineConfigurationConfigurer<UnderwritingFlowStatusEnum, FlowEventEnum> config)
            throws Exception {
        config.withConfiguration()
                .beanFactory(beanFactory)
                .listener(underwritingFlowPersisterListener())
                .machineId(FLOW_STATE_MACHINE_ID);
    }

    @Bean
    public Guard<UnderwritingFlowStatusEnum, FlowEventEnum> approvedGuard() {
        return new Guard<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow underwritingFlow = UnderwritingFlow.from(context.getExtendedState());
                StrategyStatusEnum strategyStatus = underwritingFlow.getStrategyResultStatus();
                return strategyStatus == StrategyStatusEnum.PASS && !underwritingFlow.hasNextStrategy();
            }

        };
    }

    @Bean
    public Guard<UnderwritingFlowStatusEnum, FlowEventEnum> rejectGuard() {
        return new Guard<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public boolean evaluate(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                return UnderwritingFlow.from(context.getExtendedState())
                        .getStrategyResultStatus() == StrategyStatusEnum.REJECT;
            }

        };
    }

    @Bean
    public Action<UnderwritingFlowStatusEnum, FlowEventEnum> executeStrategyAction() {
        return new Action<UnderwritingFlowStatusEnum, FlowEventEnum>() {

            @Override
            public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
                UnderwritingFlow.from(context.getExtendedState()).execute();
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
                UnderwritingFlow.from(context.getExtendedState()).setNextStrategy();
            }

        };
    }

    @Bean
    public StateMachineListener<UnderwritingFlowStatusEnum, FlowEventEnum> underwritingFlowPersisterListener() {
        return new StateMachineListenerAdapter<UnderwritingFlowStatusEnum, FlowEventEnum>() {
            @Override
            public void stateContext(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> stateContext) {
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
            public void stateMachineError(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine,
                    Exception exception) {
                exception.printStackTrace();
            }
        };
    }

}
