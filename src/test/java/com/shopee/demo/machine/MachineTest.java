package com.shopee.demo.machine;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import reactor.core.publisher.Mono;

public class MachineTest {
    public StateMachine<String, String> stateMachine() throws Exception {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .listener(listener());
        builder.configureStates()
                .withStates()
                .initial("S1")
                .stateDo("S2", a -> {
                    System.out.println("进入S2");
                    throw new IllegalStateException("非法状态");
                })
                .state("S2");
        builder.configureTransitions()
                .withExternal()
                .source("S1")
                .target("S2")
                .event("E1")
                .action(a -> {
                    System.out.println("执行策略");
                    a.getStateMachine().setStateMachineError(new IllegalStateException("策略执行失败"));
                    throw new IllegalStateException("策略执行失败");
                });
        return builder.build();
    }

    @Test
    void test() throws Exception {
        StateMachine<String, String> m = stateMachine();
        m.startReactively().block();
        m.sendEvent(Mono.just(MessageBuilder.withPayload("E1").build())).subscribe();
        System.out.println("状态机当前状态：" + m.getState().getId());
    }

    public StateMachineListener<String, String> listener() {
        return new StateMachineListenerAdapter<String, String>() {
            @Override
            public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
                System.out.println("状态机错误");
                exception.printStackTrace();
            }
        };
    }
}
