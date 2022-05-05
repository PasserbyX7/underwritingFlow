package com.shopee.demo.infrastructure.service;

public interface DistributeLockService {
    void executeWithDistributeLock(String key, Runnable runnable);
}
