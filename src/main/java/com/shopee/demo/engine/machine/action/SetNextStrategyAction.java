package com.shopee.demo.engine.machine.action;

import com.shopee.demo.engine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.entity.UnderwritingFlow;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class SetNextStrategyAction implements Action<UnderwritingFlowStatusEnum, FlowEventEnum> {

    @Override
    public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
        context.getExtendedState()
                .get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class)
                .setNextStrategy();

    }

}
