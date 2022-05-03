package com.shopee.demo.flow;

import com.shopee.demo.constant.UnderwritingFlowStatusEnum;
import com.shopee.demo.domain.entity.UnderwritingFlow;

public interface FlowStatus {

    void execute(UnderwritingFlow<?> underwritingContext);

    UnderwritingFlowStatusEnum getFlowStatus();

}
