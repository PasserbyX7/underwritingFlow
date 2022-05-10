package com.shopee.demo.engine.service.machine.impl;

import javax.annotation.Resource;

import com.google.common.collect.ImmutableMap;
import com.shopee.demo.engine.config.FlowMachineBuilder;
import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.entity.flow.UnderwritingFlow;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.machine.FlowStateMachinePersistService;

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
    public void persist(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine) throws Exception {
        underwritingFlowRepository.save(UnderwritingFlow.from(stateMachine.getExtendedState()));
    }

    @Override
    public void restore(StateMachine<FlowStatusEnum, FlowEventEnum> stateMachine, long underwritingFlowId)
            throws Exception {
        UnderwritingFlow underwritingFlow = underwritingFlowRepository.find(underwritingFlowId);
        StateMachineContext<FlowStatusEnum, FlowEventEnum> context = new DefaultStateMachineContext<FlowStatusEnum, FlowEventEnum>(
                underwritingFlow.getFlowStatus(),
                null,
                null,
                new DefaultExtendedState(ImmutableMap.of(UnderwritingFlow.EXTENDED_STATE_KEY, underwritingFlow)),
                null,
                FlowMachineBuilder.FLOW_STATE_MACHINE_ID);
        stateMachine.getStateMachineAccessor().doWithAllRegions(f -> f.resetStateMachineReactively(context).block());
    }

}
