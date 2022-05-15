package com.shopee.demo.engine.entity.flow;

import com.shopee.demo.engine.constant.DataSourceEnum;
import com.shopee.demo.engine.constant.FlowStatusEnum;
import com.shopee.demo.engine.constant.StrategyEnum;
import com.shopee.demo.engine.constant.StrategyStatusEnum;
import com.shopee.demo.engine.repository.converter.UnderwritingFlowLogConverter;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowDO;
import com.shopee.demo.infrastructure.dal.data.UnderwritingFlowLogDO;

import lombok.Data;

@Data
public class UnderwritingFlowLog {
    private Long underwritingFlowId;
    private FlowStatusEnum flowStatus;
    private StrategyEnum currentStrategy;
    private StrategyStatusEnum strategyStatus;
    private DataSourceEnum suspendDataSource;
    private String errorMsg;
    private String strategyInput;
    private String strategyOutput;

    public UnderwritingFlowLogDO convertToDO() {
        return UnderwritingFlowLogConverter.INSTANCE.convert(this);
    }

    public static UnderwritingFlowLog of(UnderwritingFlowDO flowDO) {
        return UnderwritingFlowLogConverter.INSTANCE.convert(flowDO);
    }

}
