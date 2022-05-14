package com.shopee.demo.engine.config;

import java.util.EnumSet;

import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.exception.flow.FlowException;
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
import org.springframework.statemachine.monitor.AbstractStateMachineMonitor;
import org.springframework.statemachine.monitor.StateMachineMonitor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.shopee.demo.engine.constant.FlowEventEnum.*;
import static com.shopee.demo.engine.constant.FlowStatusEnum.*;
import static com.shopee.demo.engine.constant.StrategyStatusEnum.*;

@Slf4j
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
                .source(ONGOING).target(CHOICE).event(EXECUTE)
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
                .listener(errorHandleListener())
                .machineId(FLOW_STATE_MACHINE_ID)
                .and()
                .withMonitoring()
                .monitor(stateMachineMonitor());
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
                try {
                    UnderwritingFlow.from(context.getExtendedState()).execute();
                    context.getStateMachine()
                            .sendEvent(Mono.just(MessageBuilder.withPayload(EXECUTE).build()))
                            .subscribe();
                } catch (Exception e) {
                    context.getStateMachine().setStateMachineError(new FlowException("flow execute error", e));
                }
            }

        };
    }

    @Bean
    public Action<FlowStatusEnum, FlowEventEnum> setNextStrategyAction() {
        return new Action<FlowStatusEnum, FlowEventEnum>() {

            @Override
            public void execute(StateContext<FlowStatusEnum, FlowEventEnum> context) {
                try {
                    UnderwritingFlow.from(context.getExtendedState()).setNextStrategy();
                } catch (Exception e) {
                    context.getStateMachine().setStateMachineError(new FlowException("flow set strategy error", e));
                }
            }

        };
    }

    @Bean
    public StateMachineListener<FlowStatusEnum, FlowEventEnum> underwritingFlowPersisterListener() {
        return new StateMachineListenerAdapter<FlowStatusEnum, FlowEventEnum>() {
            @Override
            public void stateContext(StateContext<FlowStatusEnum, FlowEventEnum> stateContext) {
                if (!stateContext.getStateMachine().hasStateMachineError()
                        && stateContext.getStage() == Stage.STATE_ENTRY) {
                    try {
                        UnderwritingFlow flow = UnderwritingFlow.from(stateContext.getExtendedState());
                        flow.setFlowStatus(stateContext.getStateMachine().getState().getId());
                        log.info("Persist underwriting flow[{}]", flow);
                        flowStateMachinePersistService.persist(stateContext.getStateMachine());
                    } catch (Exception e) {
                        stateContext.getStateMachine()
                                .setStateMachineError(new FlowException("Persist underwriting flow error", e));
                    }
                }
            }
        };
    }

    @Bean
    public StateMachineListener<FlowStatusEnum, FlowEventEnum> errorHandleListener() {
        return new StateMachineListenerAdapter<FlowStatusEnum, FlowEventEnum>() {
            @Override
            public void stateMachineError(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine,
                    Exception exception) {
                log.info("state machine exception[{}]", stateMachine, exception);
            }
        };
    }

    @Bean
    public StateMachineMonitor<FlowStatusEnum, FlowEventEnum> stateMachineMonitor() {
        return new AbstractStateMachineMonitor<FlowStatusEnum, FlowEventEnum>() {
            @Override
            public void transition(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine,
                    Transition<FlowStatusEnum, FlowEventEnum> transition, long duration) {
                UnderwritingFlow flow = UnderwritingFlow.from(stateMachine.getExtendedState());
                log.info("Underwriting state machine transition:[{}]->[{}] cost[{}]ms with flow[{}]",
                        transition.getSource().getId(),
                        transition.getTarget().getId(), duration, flow);
            }
        };
    }

}