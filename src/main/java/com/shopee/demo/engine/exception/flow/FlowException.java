package com.shopee.demo.engine.exception.flow;

public class FlowException extends RuntimeException {

    public FlowException(Throwable cause) {
        super(cause);
    }

    public FlowException(String message) {
        super(message);
    }

    public FlowException(String message, Throwable cause) {
        super(message, cause);
    }
}