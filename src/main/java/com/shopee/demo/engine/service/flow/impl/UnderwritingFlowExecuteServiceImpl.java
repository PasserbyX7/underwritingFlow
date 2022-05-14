package com.shopee.demo.engine.service.flow.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import com.shopee.demo.engine.config.FlowStateMachineProperties;
import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.exception.flow.FlowException;
import com.shopee.demo.engine.repository.UnderwritingFlowRepository;
import com.shopee.demo.engine.service.flow.UnderwritingFlowExecuteService;
import com.shopee.demo.engine.service.machine.FlowStateMachinePoolService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UnderwritingFlowExecuteServiceImpl implements UnderwritingFlowExecuteService {

    @Resource
    private FlowStateMachineProperties flowStateMachineProperties;

    @Resource
    private UnderwritingFlowRepository underwritingFlowRepository;

    @Resource
    private FlowStateMachinePoolService flowStateMachinePoolService;

    @Async("UnderwritingFlowPoolTaskExecutor")
    @Override
    public void executeUnderwritingFlowAsync(long underwritingFlowId) {
        try {
            String underwritingId = underwritingFlowRepository.find(underwritingFlowId)
                    .getUnderwritingRequest()
                    .getUnderwritingId();
            executeFlowWithDistributedLock(underwritingFlowId, underwritingId);
        } catch (Exception e) {
            // TODO 异常监控上报
        }
    }

    private void executeFlowWithDistributedLock(long underwritingFlowId, String underwritingId) {
        Lock lock = getDistributedLock(underwritingId);
        if (lock.tryLock()) {
            try {
                log.info("Underwriting flow[{}] start execution with distributed Lock[{}]", underwritingFlowId,
                        underwritingId);
                executeFlow(underwritingFlowId);
            } finally {
                log.info("Underwriting flow[{}] end execution with distributed Lock[{}]", underwritingFlowId,
                        underwritingId);
                lock.unlock();
            }
        } else {
            throw new FlowException("There is a underwriting flow being executed");
        }
    }

    private void executeFlow(long underwritingFlowId) {
        FlowStateMachine flowStateMachine = null;
        try {
            // 创建状态机
            flowStateMachine = flowStateMachinePoolService.acquire(underwritingFlowId);
            // 执行状态机
            flowStateMachine.execute(flowStateMachineProperties.getFlowTimeout());
        } finally {
            // 销毁状态机
            if (flowStateMachine != null) {
                flowStateMachinePoolService.release(flowStateMachine);
            }
        }
    }

    private Lock getDistributedLock(String underwritingId) {
        // TODO 根据underwritingId获取分布式锁
        // TODO 分布式key增加环境标识符
        return new ReentrantLock();
    }

}
