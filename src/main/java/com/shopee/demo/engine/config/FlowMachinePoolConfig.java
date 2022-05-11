package com.shopee.demo.engine.config;

import javax.annotation.Resource;

import com.shopee.demo.engine.entity.machine.FlowStateMachine;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowMachinePoolConfig {

    @Resource
    private FlowMachineBuilder flowMachineBuilder;

    @Bean
    public FlowMachinePool flowMachinePool() {
        BasePooledObjectFactory<FlowStateMachine> pooledFlowMachineFactory = pooledFlowMachineFactory();
        GenericObjectPool<FlowStateMachine> flowMachinePool = new GenericObjectPool<>(pooledFlowMachineFactory);
        return new FlowMachinePool(flowMachinePool);
    }

    @Bean
    public BasePooledObjectFactory<FlowStateMachine> pooledFlowMachineFactory() {
        return new BasePooledObjectFactory<FlowStateMachine>() {
            @Override
            public FlowStateMachine create() throws Exception {
                return FlowStateMachine.of(flowMachineBuilder.build());
            }

            @Override
            public void passivateObject(PooledObject<FlowStateMachine> p) throws Exception {
                p.getObject().stop();
            }

            @Override
            public PooledObject<FlowStateMachine> wrap(FlowStateMachine machine) {
                return new DefaultPooledObject<FlowStateMachine>(machine);
            }
        };
    }

    public static class FlowMachinePool {
        private final GenericObjectPool<FlowStateMachine> pool;

        public FlowMachinePool(GenericObjectPool<FlowStateMachine> pool) {
            this.pool = pool;
        }

        public FlowStateMachine borrowObject() throws Exception {
            return pool.borrowObject();
        }

        public void returnObject(FlowStateMachine flowStateMachine) throws Exception {
            pool.returnObject(flowStateMachine);
        }

    }

}
