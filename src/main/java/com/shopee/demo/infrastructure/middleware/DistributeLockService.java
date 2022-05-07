package com.shopee.demo.infrastructure.middleware;

import java.util.concurrent.Callable;

public interface DistributeLockService {
    Object executeWithDistributeLock(String key, Callable<Object> runnable);
}
