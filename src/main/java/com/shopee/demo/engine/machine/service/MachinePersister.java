package com.shopee.demo.engine.machine.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.entity.UnderwritingFlow;
import com.shopee.demo.engine.domain.repository.UnderwritingFlowRepository;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class MachinePersister implements StateMachinePersist<UnderwritingFlowStatusEnum, FlowEventEnum, Long> {

    @Resource
    private UnderwritingFlowRepository contextService;

    @Override
    public void write(StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> context, Long contextId)
            throws Exception {
        UnderwritingFlow<?> underwritingContext = context.getExtendedState().get(ExtendedStateEnum.UNDERWRITING_CONTEXT,
                UnderwritingFlow.class);
        contextService.save(underwritingContext);
    }

    @Override
    public StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> read(Long contextId) throws Exception {
        UnderwritingFlow<?> underwritingContext = contextService.load(contextId);
        UnderwritingFlowStatusEnum currentStatus = UnderwritingFlowStatusEnum
                .valueOf(underwritingContext.getFlowStatus().toString());
        Map<Object, Object> variables = new HashMap<>();
        variables.put(ExtendedStateEnum.UNDERWRITING_CONTEXT, underwritingContext);
        ExtendedState extendedState = new DefaultExtendedState(variables);
        StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> result = new DefaultStateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum>(
                currentStatus,
                null,
                null,
                extendedState,
                null,
                "UnderwritingFlowMachine");
        return result;
    }

}
