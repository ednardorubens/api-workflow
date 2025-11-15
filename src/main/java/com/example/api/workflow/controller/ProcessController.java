package com.example.api.workflow.controller;

import com.example.api.workflow.controller.request.CreateProcessRequest;
import com.example.api.workflow.controller.request.TransitionRequest;
import com.example.api.workflow.controller.response.OrderProcessResponse;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.service.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
public class ProcessController {

    private final ProcessService processService;

    @PostMapping
    public OrderProcessResponse createProcess(@Valid @RequestBody CreateProcessRequest createProcessRequest) {
        return processService.saveProcess(createProcessRequest);
    }

    @PostMapping("/{processId}/transition")
    public OrderProcessResponse processTransition(
            @PathVariable String processId,
            @Valid @RequestBody TransitionRequest transitionRequest) throws TransitionException {
        return processService.processTransition(transitionRequest.withProcessId(processId));
    }

}
