package com.shopee.demo.persister;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.shopee.demo.constant.ExtendedStateEnum;
import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.context.UnderwritingFlow;
import com.shopee.demo.service.UnderwritingContextService;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class MachinePersister implements StateMachinePersist<UnderwritingFlowStatusEnum, FlowEventEnum, Long> {

    @Resource
    private UnderwritingContextService contextService;

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
        // return map.get(contextId);
    }

}
