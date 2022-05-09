package com.shopee.demo.engine.entity.config;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.machine.FlowStateMachine;
import com.shopee.demo.engine.type.flow.FlowEventEnum;
import com.shopee.demo.engine.type.flow.UnderwritingFlowStatusEnum;
import com.shopee.demo.engine.type.machine.MachineId;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

@Configuration
public class FlowMachinePoolConfig {

    @Resource
    private BasePooledObjectFactory<FlowStateMachine> pooledFlowMachineFactory;

    @Bean
    public GenericObjectPool<FlowStateMachine> flowMachinePool() {
        GenericObjectPool<FlowStateMachine> flowMachinePool = new GenericObjectPool<>(pooledFlowMachineFactory);
        return flowMachinePool;
    }

    @Component
    public static class PooledFlowMachineFactory extends BasePooledObjectFactory<FlowStateMachine> {

        @Resource
        private StateMachineFactory<UnderwritingFlowStatusEnum, FlowEventEnum> stateMachineFactory;

        @Override
        public FlowStateMachine create() throws Exception {
            return FlowStateMachine.of(stateMachineFactory.getStateMachine(MachineId.UNDERWRITING_FLOW_ID));
        }

        @Override
        public void passivateObject(PooledObject<FlowStateMachine> p) throws Exception {
            p.getObject().stop();
        }

        @Override
        public PooledObject<FlowStateMachine> wrap(FlowStateMachine machine) {
            return new DefaultPooledObject<FlowStateMachine>(machine);
        }

    }

}
