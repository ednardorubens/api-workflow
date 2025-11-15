package com.example.api.workflow.mapper;

import com.example.api.workflow.controller.response.OrderProcessResponse;
import com.example.api.workflow.controller.response.ProcessHistoryResponse;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessHistory;

public class OrderProcessMapper {

    private OrderProcessMapper() {
    }

    public static OrderProcessResponse parseOrderProcessResponse(OrderProcess orderProcess) {
        return new OrderProcessResponse(
            orderProcess.id(),
            orderProcess.definition(),
            orderProcess.state(),
            orderProcess.history().stream()
                .map(OrderProcessMapper::parseProcessHistoryResponse)
                .toList());
    }

    public static ProcessHistoryResponse parseProcessHistoryResponse(ProcessHistory history) {
        return new ProcessHistoryResponse(
            history.from(),
            history.to(),
            history.claimant(),
            history.timestamp());
    }

}
