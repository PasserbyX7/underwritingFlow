package com.shopee.demo.service;

public interface DistributeLockService {
    void executeWithDistributeLock(String key, Runnable runnable);
}
