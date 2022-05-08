package com.shopee.demo.pool;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

public class MachineFactory extends BaseKeyedPooledObjectFactory<String, Machine> {

    @Override
    public Machine create(String key) throws Exception {
        return new Machine(key);
    }

    @Override
    public void activateObject(String key, PooledObject<Machine> p) throws Exception {
        System.out.println("激活对象");
    }

    @Override
    public PooledObject<Machine> wrap(Machine machine) {
        return new DefaultPooledObject<>(machine);
    }

    public static void main(String[] args) {
        GenericKeyedObjectPool<String, Machine> pool = new GenericKeyedObjectPool<>(new MachineFactory());
        Machine machine = null;
        String key = "id";
        try {
            machine = pool.borrowObject(key);
            System.out.println(machine);
        } catch (Exception e) {
        } finally {
            if (machine != null) {
                pool.returnObject(key, machine);
            }
        }
    }
}