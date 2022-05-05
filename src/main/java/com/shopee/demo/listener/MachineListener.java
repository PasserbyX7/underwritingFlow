package com.shopee.demo.listener;

import javax.annotation.Resource;

import com.shopee.demo.constant.ExtendedStateEnum;
import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.domain.entity.UnderwritingFlow;
import com.shopee.demo.domain.repository.UnderwritingFlowRepository;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnStateEntry;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine(id = "UnderwritingFlowMachine")
public class MachineListener {

    @Resource
    private UnderwritingFlowRepository contextService;

    @OnStateEntry
    public void anyTransition(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) {
        UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(ExtendedStateEnum.UNDERWRITING_CONTEXT,
        UnderwritingFlow.class);
        UnderwritingFlowStatusEnum status = context.getStateMachine().getState().getId();
        underwritingContext.setFlowStatus(status);
        contextService.save(underwritingContext);
    }

}
