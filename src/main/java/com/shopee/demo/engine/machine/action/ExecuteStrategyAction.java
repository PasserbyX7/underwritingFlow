package com.shopee.demo.engine.machine.action;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.machine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class ExecuteStrategyAction implements Action<UnderwritingFlowStatusEnum, FlowEventEnum> {

    @Override
    public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
        context.getExtendedState()
                .get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class)
                .execute();
        context.getStateMachine()
                .sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.STRATEGY_EXECUTE).build())).subscribe();
    }

}
