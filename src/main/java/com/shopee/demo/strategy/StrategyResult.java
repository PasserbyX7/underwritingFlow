package com.shopee.demo.strategy;

import com.shopee.demo.constant.DataSourceEnum;
import com.shopee.demo.constant.StrategyStatusEnum;
import com.shopee.demo.constant.StrategyTerminalReasonEnum;

import lombok.Data;

@Data
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

}
