package com.shopee.demo.engine.constant;

public enum StrategyTerminalReasonEnum {
    /**
     * 数据不合法,请求字段缺失,从外部请求不到数据
     */
    DATA_ILLEGAL,
    /**
     * 授信流程配置有问题
     */
    FLOW_ILLEGAL,
    /**
     * 请求超时,如请求外部数据源超过最大重试次数仍然不成功
     */
    REQUEST_TIMEOUT
}
