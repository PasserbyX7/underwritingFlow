package com.shopee.demo.infrastructure.middleware.impl;

import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DistributeLockServiceImpl implements DistributeLockService {

    @Override
    public void executeWithDistributeLock(String key, Runnable runnable) {
        log.info("获取布式锁：[{}]", key);
        runnable.run();
        log.info("释放布式锁：[{}]", key);
    }

}
