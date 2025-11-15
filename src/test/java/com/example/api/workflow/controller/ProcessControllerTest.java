package com.example.api.workflow.controller;

import com.example.api.workflow.controller.request.CreateProcessRequest;
import com.example.api.workflow.controller.request.TransitionRequest;
import com.example.api.workflow.controller.response.OrderProcessResponse;
import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.service.ProcessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProcessControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProcessService processService;

    @Test
    void createProcessShouldDelegateToServiceAndReturnBadRequest() throws Exception {
        mockMvc.perform(post("/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Falha na validação de atributos"))
                .andExpect(jsonPath("$.errors[0]").value("Claimant não deve estar em branco"))
                .andExpect(jsonPath("$.errors[1]").value("Definition não deve estar em branco"));

        verifyNoMoreInteractions(processService);
    }

    @Test
    void createProcessShouldDelegateToServiceAndReturnOk() throws Exception {
        var createProcessRequest = new CreateProcessRequest("José Carlos", "novo processo");
        var requestJson = objectMapper.writeValueAsString(createProcessRequest);

        mockMvc.perform(post("/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        verify(processService, times(1)).saveProcess(any(CreateProcessRequest.class));
        verifyNoMoreInteractions(processService);
    }

    @Test
    void processTransitionShouldSetProcessIdAndCallService() throws Exception {
        var processId = "123";
        var createProcessRequest = new TransitionRequest(ProcessState.UNDER_REVIEW, "José Carlos");
        var requestJson = objectMapper.writeValueAsString(createProcessRequest);

        var orderProcessResponse = new OrderProcessResponse(processId, "novo processo", ProcessState.UNDER_REVIEW, Collections.emptyList());
        when(processService.processTransition(any(TransitionRequest.class))).thenReturn(orderProcessResponse);

        mockMvc.perform(post("/process/{processId}/transition", processId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        ArgumentCaptor<TransitionRequest> captor = ArgumentCaptor.forClass(TransitionRequest.class);
        verify(processService, times(1)).processTransition(captor.capture());

        var capturedProcessId = captor.getValue().processId();

        assertEquals(processId, capturedProcessId);
        verifyNoMoreInteractions(processService);
    }

    @Test
    void processTransitionShouldReturnServerErrorWhenServiceThrows() throws Exception {
        var processId = "123";
        var createProcessRequest = new TransitionRequest(ProcessState.UNDER_REVIEW, "José Carlos");
        var requestJson = objectMapper.writeValueAsString(createProcessRequest);

        var message = "erro de transição";
        doThrow(new TransitionException(message)).when(processService).processTransition(any(TransitionRequest.class));

        mockMvc.perform(post("/process/{processId}/transition", processId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Falha na transição de status"))
                .andExpect(jsonPath("$.errors[0]").value(message));

        verify(processService, times(1)).processTransition(any(TransitionRequest.class));
        verifyNoMoreInteractions(processService);
    }

}