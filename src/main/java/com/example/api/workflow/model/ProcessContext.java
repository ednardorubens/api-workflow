package com.example.api.workflow.model;

import com.example.api.workflow.enums.ProcessState;

public class ProcessContext {

    private final String claimant;
    private OrderProcess orderProcess;

    public ProcessContext(String claimant, OrderProcess orderProcess) {
        this.claimant = claimant;
        this.orderProcess = orderProcess;
    }

    public ProcessContext withState(ProcessState newState) {
        this.orderProcess = this.orderProcess.withState(newState);
        return this;
    }

    public String claimant() {
        return claimant;
    }

    public OrderProcess orderProcess() {
        return orderProcess;
    }

}
