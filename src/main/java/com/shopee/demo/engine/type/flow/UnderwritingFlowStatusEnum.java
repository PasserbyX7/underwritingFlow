package com.shopee.demo.engine.type.flow;

public enum UnderwritingFlowStatusEnum {
    INITIAL,
    ONGOING,
    CHOICE,
    PENDING,
    APPROVED,
    REJECTED,
    EXPIRED,
    CANCELLED;

    public boolean isTerminal() {
        return this == APPROVED || this == REJECTED || this == EXPIRED || this == CANCELLED;
    }

}
