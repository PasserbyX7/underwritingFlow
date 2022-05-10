package com.shopee.demo.engine.type.strategy;

import static com.shopee.demo.engine.constant.StrategyStatusEnum.*;

import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;

import lombok.Value;

@Value
public class StrategyResult {

    private StrategyStatusEnum status;
    /**
     * 当status为suspend时有值
     */
    private DataSourceEnum suspendDataSource;
    /**
     * 当status为error时有值
     */
    private String errorMsg;

    public static StrategyResult pass() {
        return new StrategyResult(PASS, null, null);
    }

    public static StrategyResult suspend(DataSourceEnum suspendDataSource) {
        return new StrategyResult(SUSPEND, suspendDataSource, null);
    }

    public static StrategyResult error(String errorMsg) {
        return new StrategyResult(ERROR, null, errorMsg);
    }

    public static StrategyResult reject() {
        return new StrategyResult(REJECT, null, null);
    }

}
