package com.example.api.workflow.service;

import com.example.api.workflow.config.TransitionRegistry;
import com.example.api.workflow.controller.request.CreateProcessRequest;
import com.example.api.workflow.controller.request.TransitionRequest;
import com.example.api.workflow.controller.response.OrderProcessResponse;
import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.mapper.OrderProcessMapper;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;
import com.example.api.workflow.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessService {

    private final TransitionRegistry registry;
    private final ProcessRepository processRepository;

    public OrderProcessResponse saveProcess(CreateProcessRequest createProcessRequest) {
        log.info("Salvando novo processo com definição: {}", createProcessRequest.definition());
        return OrderProcessMapper.parseOrderProcessResponse(
            processRepository.save(
                OrderProcess.withDefinitionAndState(
                    createProcessRequest.claimant(),
                    createProcessRequest.definition(),
                    ProcessState.STARTED)));
    }

    public OrderProcessResponse processTransition(TransitionRequest transitionRequest) throws TransitionException {
        log.info("Processando transição do processo: {} para o status: {}", transitionRequest.processId(), transitionRequest.newState());

        var orderProcess = processRepository.findById(transitionRequest.processId())
                .orElseThrow(() -> new TransitionException("Processo com ID " + transitionRequest.processId() + " não encontrado."));

        var processContext = new ProcessContext(transitionRequest.claimant(), orderProcess);

        var handler = registry.getHandler(orderProcess.state(), transitionRequest.newState())
                .orElseThrow(() -> new TransitionException("Transição não permitida de " + orderProcess.state() + " para " + transitionRequest.newState()));

        handler.onTransition(processContext);

        log.info("Transição processada com sucesso para processo {}: {} -> {}", transitionRequest.processId(), orderProcess.state(), transitionRequest.newState());

        return OrderProcessMapper.parseOrderProcessResponse(orderProcess);
    }

}
