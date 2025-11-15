package com.example.api.workflow.aspect;

import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.handler.TransitionHandler;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;
import com.example.api.workflow.model.ProcessHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransitionHistoryAspectTest {

    @Autowired
    private TransitionHandler startToUnderReviewTransition;

    @Autowired
    private TransitionHandler startToAprovedTransition;

    @Autowired
    private TransitionHandler underReviewToAprovedTransition;

    @Test
    void testTransitionHistoryIsRecorded() throws TransitionException {
        var orderProcess = OrderProcess.withDefinitionAndState("usuario1", "process1", ProcessState.STARTED);
        var processContext = new ProcessContext("usuario1", orderProcess);

        startToUnderReviewTransition.onTransition(processContext);

        underReviewToAprovedTransition.onTransition(processContext);

        List<ProcessHistory> histories = orderProcess.history();
        assertFalse(histories.isEmpty());
        assertEquals(2, histories.size());
        var processHistory = histories.get(0);
        assertEquals(processContext.claimant(), processHistory.claimant());
        assertEquals(ProcessState.STARTED, processHistory.from());
        assertEquals(ProcessState.UNDER_REVIEW, processHistory.to());
    }

    @Test
    void testTransitionHistoryThrowException() throws TransitionException {
        var orderProcess = OrderProcess.withDefinitionAndState("usuario1", "process1", ProcessState.STARTED);
        var processContext = new ProcessContext(
                "usuario1",
                orderProcess
        );

        startToAprovedTransition.onTransition(processContext);

        var ex = assertThrows(TransitionException.class, () -> underReviewToAprovedTransition.onTransition(processContext));

        List<ProcessHistory> histories = orderProcess.history();
        assertFalse(histories.isEmpty());
        assertEquals(1, histories.size());

        var processHistory = histories.get(0);
        assertEquals(processContext.claimant(), processHistory.claimant());
        assertEquals(ProcessState.STARTED, processHistory.from());
        assertEquals(ProcessState.APROVED, processHistory.to());

        assertEquals("O processo %s tem status(APROVED) diferente do status(UNDER_REVIEW) requerido".formatted(orderProcess.id()), ex.getMessage());
    }

}
