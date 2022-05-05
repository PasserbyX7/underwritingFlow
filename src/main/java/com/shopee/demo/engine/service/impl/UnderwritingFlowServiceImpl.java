package com.shopee.demo.engine.service.impl;

import javax.annotation.Resource;

import com.shopee.demo.engine.constant.ExtendedStateEnum;
import com.shopee.demo.engine.constant.FlowEventEnum;
import com.shopee.demo.engine.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.domain.entity.UnderwritingFlow;
import com.shopee.demo.engine.domain.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.domain.type.request.UnderwritingRequest;
import com.shopee.demo.engine.machine.service.FlowStateMachineService;
import com.shopee.demo.engine.service.UnderwritingFlowService;
import com.shopee.demo.infrastructure.service.DistributeLockService;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UnderwritingFlowServiceImpl implements UnderwritingFlowService {

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private DistributeLockService distributeLockService;

    @Resource
    private FlowStateMachineService flowStateMachineService;

    @Override
    public long createUnderwritingTask(UnderwritingRequest underwritingRequest) {
        UnderwritingFlow<?> underwritingContext = UnderwritingFlow.of(underwritingRequest);
        return underwritingFlowRepository.save(underwritingContext);
    }

    @Override
    public void executeUnderwritingTask(long underwritingFlowId) {
        // 销毁状态机
        UnderwritingFlow<?> underwritingFlow = underwritingFlowRepository.load(underwritingFlowId);
        String underwritingId = underwritingFlow.getUnderwritingRequest().getUnderwritingId();
        distributeLockService.executeWithDistributeLock(underwritingId, () -> {
            // 创建状态机
            StateMachine<UnderwritingFlowStatusEnum, FlowEventEnum> machine = flowStateMachineService
                    .acquireStateMachine(underwritingFlowId);
            // 执行状态机
            machine.sendEvent(Mono.just(MessageBuilder.withPayload(FlowEventEnum.START)
                    .build())).blockLast();
            while (machine.getState().getId() == UnderwritingFlowStatusEnum.ONGOING) {
                try {
                    //等待状态机执行完毕
                    machine.getExtendedState()
                            .get(ExtendedStateEnum.UNDERWRITING_CONTEXT, UnderwritingFlow.class)
                            .getLatch()
                            .await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //销毁状态机
            flowStateMachineService.releaseStateMachine(underwritingFlowId);
            log.info("运行结束：[{}][{}]", machine.getId(), machine.getState().getId());
        });
    }

}
