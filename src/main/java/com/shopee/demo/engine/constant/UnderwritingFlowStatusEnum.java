package com.shopee.demo.engine.constant;

public enum UnderwritingFlowStatusEnum {
    CREATED,
    ONGOING,
    CHOICE,
    PENDING,
    APPROVED,
    REJECT,
    EXPIRED,
    CANCELLED;
    public boolean isTerminal(){
        //TODO
        if(this==APPROVED)
            return true;
        return false;
    }
}
