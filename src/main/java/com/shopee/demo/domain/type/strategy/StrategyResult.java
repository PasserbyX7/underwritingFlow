package com.shopee.demo.domain.type.strategy;

import com.shopee.demo.constant.DataSourceEnum;
import com.shopee.demo.constant.StrategyStatusEnum;
import com.shopee.demo.constant.StrategyTerminalReasonEnum;

import lombok.Value;

import static com.shopee.demo.constant.StrategyStatusEnum.*;

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
        return new StrategyResult(TERMINAL, null, terminalReason);
    }

}
