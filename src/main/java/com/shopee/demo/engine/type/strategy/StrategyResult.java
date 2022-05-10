package com.shopee.demo.engine.type.strategy;

import static com.shopee.demo.engine.type.strategy.StrategyStatusEnum.*;

import lombok.Value;

@Value
public class StrategyResult {

    private StrategyStatusEnum status;
    /**
     * 当status为suspend时有值
     */
    private DataSourceEnum suspendDataSource;
    /**
     * 当status为terminal时有值
     */
    private StrategyTerminalReasonEnum terminalReason;

    public static StrategyResult pass() {
        return new StrategyResult(PASS, null, null);
    }

    public static StrategyResult suspend(DataSourceEnum suspendDataSource) {
        return new StrategyResult(SUSPEND, suspendDataSource, null);
    }

    public static StrategyResult terminal(StrategyTerminalReasonEnum terminalReason) {
        return new StrategyResult(ERROR, null, terminalReason);
    }

    public static StrategyResult reject() {
        return new StrategyResult(REJECT, null, null);
    }

}
