package com.shopee.demo.engine.machine.listener;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.machine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.machine.constant.MachineId;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnStateEntry;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine(id = MachineId.UNDERWRITING_FLOW_ID)
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
