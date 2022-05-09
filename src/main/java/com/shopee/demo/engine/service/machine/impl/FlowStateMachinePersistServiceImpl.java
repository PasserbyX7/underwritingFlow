package com.shopee.demo.engine.service.machine.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.machine.ExtendedStateEnum;
import com.shopee.demo.engine.type.machine.MachineId;

import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class FlowStateMachinePersistServiceImpl implements FlowStateMachinePersistService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Override
    public void write(StateContext<UnderwritingFlowStatusEnum, FlowEventEnum> context) throws Exception {
        UnderwritingFlow underwritingFlow = context.getExtendedState().get(ExtendedStateEnum.UNDERWRITING_CONTEXT,
                UnderwritingFlow.class);
        underwritingFlowRepository.save(underwritingFlow);
    }

    @Override
    public StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> read(long underwritingFlowId)
            throws Exception {
        UnderwritingFlow underwritingFlow = underwritingFlowRepository.find(underwritingFlowId);
        UnderwritingFlowStatusEnum currentStatus = underwritingFlow.getFlowStatus();
        Map<Object, Object> variables = new HashMap<>();
        variables.put(ExtendedStateEnum.UNDERWRITING_CONTEXT, underwritingFlow);
        ExtendedState extendedState = new DefaultExtendedState(variables);
        StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> result = new DefaultStateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum>(
                currentStatus,
                null,
                null,
                extendedState,
                null,
                MachineId.UNDERWRITING_FLOW_ID);
        return result;
    }

}
