package com.example.api.workflow.service;

import com.example.api.workflow.config.TransitionRegistry;
import com.example.api.workflow.controller.request.CreateProcessRequest;
import com.example.api.workflow.controller.request.TransitionRequest;
import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.handler.TransitionHandler;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;
import com.example.api.workflow.model.ProcessHistory;
import com.example.api.workflow.repository.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessServiceTest {

    @Mock
    private TransitionRegistry registry;

    @Mock
    private ProcessRepository processRepository;

    private ProcessService processService;

    @BeforeEach
    void setUp() {
        processService = new ProcessService(registry, processRepository);
    }

    @Test
    void saveProcessShoulCallRepository() {
        var req = new CreateProcessRequest("claimant-1", "my-definition");

        var saved = OrderProcess.withDefinitionAndState("claimant-1", "my-definition", ProcessState.STARTED);
        when(processRepository.save(any(OrderProcess.class))).thenReturn(saved);

        var response = processService.saveProcess(req);

        // Verificações mínimas: repository foi chamado e não ocorreu exceção
        ArgumentCaptor<OrderProcess> captor = ArgumentCaptor.forClass(OrderProcess.class);
        verify(processRepository, times(1)).save(captor.capture());

        assertNotNull(captor.getValue());
        assertNotNull(response);
        verifyNoMoreInteractions(processRepository, registry);
    }

    @Test
    void processTransitionShouldThrowWhenProcessNotFound() {
        TransitionRequest tr = mock(TransitionRequest.class);
        when(tr.processId()).thenReturn("missing-id");

        when(processRepository.findById("missing-id")).thenReturn(Optional.empty());

        TransitionException ex = assertThrows(TransitionException.class, () -> processService.processTransition(tr));
        assertTrue(ex.getMessage().contains("missing-id"));
        verify(processRepository, times(1)).findById("missing-id");
        verifyNoInteractions(registry);
    }

    @Test
    void processTransitionShouldThrowWhenHandlerNotFound() {
        TransitionRequest tr = mock(TransitionRequest.class);
        when(tr.processId()).thenReturn("p1");
        when(tr.newState()).thenReturn(ProcessState.REPROVED);

        OrderProcess existing = OrderProcess.withDefinitionAndState("app", "def", ProcessState.STARTED);
        when(processRepository.findById("p1")).thenReturn(Optional.of(existing));

        when(registry.getHandler(existing.state(), ProcessState.REPROVED)).thenReturn(Optional.empty());

        TransitionException ex = assertThrows(TransitionException.class, () -> processService.processTransition(tr));
        assertTrue(ex.getMessage().contains("Transição não permitida") || ex.getMessage().toLowerCase().contains("não permitida"));
        verify(processRepository, times(1)).findById("p1");
        verify(registry, times(1)).getHandler(existing.state(), ProcessState.REPROVED);
    }

    @Test
    void processTransitionShouldExecuteHandlerWhenAvailable() throws Exception {
        var tr = new TransitionRequest("p1", ProcessState.REPROVED, "app");
        var processHistory = new ProcessHistory(ProcessState.STARTED, ProcessState.UNDER_REVIEW, "José Carlos");
        var existing = OrderProcess.withDefinitionAndState("app", "def", ProcessState.STARTED).withHistory(processHistory);
        when(processRepository.findById("p1")).thenReturn(Optional.of(existing));

        var handler = mock(TransitionHandler.class);
        when(registry.getHandler(existing.state(), ProcessState.REPROVED)).thenReturn(Optional.of(handler));

        when(handler.onTransition(any(ProcessContext.class))).thenReturn(existing);

        var orderProcessResponse = processService.processTransition(tr);

        ArgumentCaptor<ProcessContext> ctxCaptor = ArgumentCaptor.forClass(ProcessContext.class);
        verify(handler, times(1)).onTransition(ctxCaptor.capture());

        assertNotNull(ctxCaptor.getValue());
        assertNotNull(orderProcessResponse);
        verify(processRepository, times(1)).findById("p1");
        verifyNoMoreInteractions(processRepository, registry, handler);
    }

}
