package com.shopee.demo.engine.service.machine.impl;

import javax.annotation.Resource;

import com.google.common.collect.ImmutableMap;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.entity.machine.config.FlowMachineBuilder;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.machine.ExtendedStateEnum;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class FlowStateMachinePersistServiceImpl implements FlowStateMachinePersistService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Override
    public void persist(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine) throws Exception {
        underwritingFlowRepository.save(UnderwritingFlow.from(stateMachine.getExtendedState()));
    }

    @Override
    public void restore(StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachine, long underwritingFlowId)
            throws Exception {
        UnderwritingFlow underwritingFlow = underwritingFlowRepository.find(underwritingFlowId);
        StateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum> context = new DefaultStateMachineContext<UnderwritingFlowStatusEnum, FlowEventEnum>(
                underwritingFlow.getFlowStatus(),
                null,
                null,
                new DefaultExtendedState(ImmutableMap.of(ExtendedStateEnum.UNDERWRITING_CONTEXT, underwritingFlow)),
                null,
                FlowMachineBuilder.FLOW_STATE_MACHINE_ID);
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(function -> function.resetStateMachineReactively(context).block());
    }

}
