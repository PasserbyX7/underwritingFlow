package com.shopee.demo.action;

import com.shopee.demo.constant.ExtendedStateEnum;
import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.context.UnderwritingFlow;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class SetNextStrategyAction implements Action<UnderwritingFlowStatusEnum, FlowEventEnum> {

    @Override
    public void execute(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
        UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(ExtendedStateEnum.UNDERWRITING_CONTEXT,
                UnderwritingFlow.class);
        underwritingContext.getStrategyContext()
                .setCurrentStrategy(underwritingContext.getCurrentStrategy().getNextStrategy().getStrategyName());
    }

}
