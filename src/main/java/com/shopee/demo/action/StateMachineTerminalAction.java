package com.shopee.demo.action;

import com.shopee.demo.constant.ExtendedStateEnum;
import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.context.UnderwritingFlow;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class StateMachineTerminalAction implements Action<UnderwritingFlowStatusEnum, FlowEventEnum> {

    @Override
    public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
        UnderwritingFlow<?> flow = context.getExtendedState().get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class);
        flow.getLatch().countDown();
    }

}
