package com.example.api.workflow.model;

import com.example.api.workflow.enums.ProcessState;

import java.time.LocalDateTime;

public class ProcessHistory {

    private final ProcessState from;
    private final ProcessState to;
    private final String claimant;
    private final LocalDateTime timestamp;

    public ProcessHistory(
        ProcessState from,
        ProcessState to,
        String claimant) {
        this.from = from;
        this.to = to;
        this.claimant = claimant;
        this.timestamp = LocalDateTime.now();
    }

    public ProcessState from() {
        return from;
    }

    public ProcessState to() {
        return to;
    }

    public String claimant() {
        return claimant;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }

}
