package com.shopee.demo.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("underwriting.flow")
public class FlowStateMachineProperties {

    /**
     * 授信超时时间（单位毫秒）
     */
    private Long flowTimeout = 30000L;
}
