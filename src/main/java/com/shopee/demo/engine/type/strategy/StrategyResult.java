package com.shopee.demo.engine.type.strategy;

import static com.shopee.demo.engine.constant.StrategyStatusEnum.*;

import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
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
    /**
     * 当status为reject时有值
     */
    private StrategyEnum rejectedStrategy;

    public static StrategyResult pass() {
        return new StrategyResult(PASS, null, null, null);
    }

    public static StrategyResult suspend(DataSourceEnum suspendDataSource) {
        return new StrategyResult(SUSPEND, suspendDataSource, null, null);
    }

    public static StrategyResult error(String errorMsg) {
        return new StrategyResult(ERROR, null, errorMsg, null);
    }

    public static StrategyResult reject(StrategyEnum rejectedStrategy) {
        return new StrategyResult(REJECT, null, null, rejectedStrategy);
    }

}
