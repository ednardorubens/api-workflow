package com.example.api.workflow.controller.response;

import com.example.api.workflow.enums.ProcessState;

import java.time.LocalDateTime;

public record ProcessHistoryResponse(
        ProcessState from,
        ProcessState to,
        String claimant,
        LocalDateTime timestamp) {
}
