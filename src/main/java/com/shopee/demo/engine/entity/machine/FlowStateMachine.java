package com.shopee.demo.engine.entity.machine;

import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;

import org.springframework.statemachine.StateMachine;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlowStateMachine {
    private StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine;
}
