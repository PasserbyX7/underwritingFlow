package com.shopee.demo.engine.constant;

public enum FlowStatusEnum {
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
