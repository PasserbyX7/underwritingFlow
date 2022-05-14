package com.shopee.demo.infrastructure.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class SpringAsyncConfig {

    @Bean
    public Executor UnderwritingFlowPoolTaskExecutor(){
        return Executors.newFixedThreadPool(5);
    }
}
