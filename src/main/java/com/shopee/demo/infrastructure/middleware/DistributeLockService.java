package com.shopee.demo.infrastructure.middleware;

public interface DistributeLockService {
    void executeWithDistributeLock(String key, Runnable runnable);
}
