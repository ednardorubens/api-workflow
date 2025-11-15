package com.example.api.workflow.controller.request;

import jakarta.validation.constraints.NotNull;
import com.example.api.workflow.enums.ProcessState;
import jakarta.validation.constraints.NotBlank;

public record TransitionRequest(
        String processId,
        @NotNull ProcessState newState,
        @NotBlank String claimant
) {

    public TransitionRequest(ProcessState newState, String claimant) {
        this(null, newState, claimant);
    }

    public TransitionRequest withProcessId(String processId) {
        return new TransitionRequest(processId, this.newState, this.claimant);
    }

}
