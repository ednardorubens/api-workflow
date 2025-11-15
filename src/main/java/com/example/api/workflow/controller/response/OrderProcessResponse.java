package com.example.api.workflow.controller.response;

import com.example.api.workflow.enums.ProcessState;

import java.util.List;

public record OrderProcessResponse(
        String id,
        String definition,
        ProcessState actualState,
        List<ProcessHistoryResponse> history) {
}
