package com.shopee.demo.constant;

public enum StrategyTerminalReasonEnum {
    STRATEGY_REJECT, //策略拒绝
    DATA_ILLEGAL, //数据不合法,请求字段缺失,从外部请求不到数据
    FLOW_ILLEGAL, //授信流程配置有问题
    REQUEST_TIMEOUT, //请求超时,如请求外部数据源超过最大重试次数仍然不成功
    UNDERWRITIGN_TIMEOUT //授信超时
}
