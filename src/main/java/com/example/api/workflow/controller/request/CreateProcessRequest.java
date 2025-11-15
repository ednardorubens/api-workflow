package com.example.api.workflow.controller.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProcessRequest(
        @NotBlank String definition,
        @NotBlank String claimant) {
}
