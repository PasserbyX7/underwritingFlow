package com.shopee.demo.flow;

import com.shopee.demo.constant.ExtendedStateEnum;
import com.shopee.demo.constant.FlowEventEnum;
import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.context.UnderwritingFlow;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.shopee.demo.constant.UnderwritingFlowStatusEnum.*;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class FlowMachine {
    private final StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine;

    public void execute() {
        UnderwritingFlow<?> flow = machine.getExtendedState().get(ExtendedStateEnum.UNDERWRITING_CONTEXT,
                UnderwritingFlow.class);
        machine.sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.START)
                .build())).blockLast();
        while (machine.getState().getId() == ONGOING) {
            try {
                flow.getLatch().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("运行结束：[{}][{}]", machine.getId(), machine.getState().getId());
    }

}
