package com.shopee.demo.infrastructure.middleware.impl;

import java.util.concurrent.Callable;

import com.shopee.demo.infrastructure.middleware.DistributeLockService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DistributeLockServiceImpl implements DistributeLockService {

    @Override
    public Object executeWithDistributeLock(String key, Callable<Object> callable) {
        log.info("获取布式锁：[{}]", key);
        Object ret = null;
        try {
            ret = callable.call();
            return ret;
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            log.info("释放布式锁：[{}]", key);
        }
    }

}
