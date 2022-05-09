package com.shopee.demo.engine.type.flow;

public enum UnderwritingFlowStatusEnum {
    INITIAL,
    ONGOING,
    CHOICE,
    PENDING,
    APPROVED,
    REJECT,
    EXPIRED,
    CANCELLED;

    public boolean isTerminal() {
        return this == APPROVED || this == REJECT || this == EXPIRED || this == CANCELLED;
    }

}
