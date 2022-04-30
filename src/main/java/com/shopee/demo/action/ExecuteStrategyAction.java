package com.shopee.demo.action;

import com.shopee.demo.constant.ExtendedStateEnum;
import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.context.UnderwritingFlow;

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
