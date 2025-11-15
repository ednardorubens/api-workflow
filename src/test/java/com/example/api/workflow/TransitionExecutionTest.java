package com.example.api.workflow;

import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.handler.StartToUnderReviewTransition;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TransitionExecutionTest {

    @Test
    void testTransitionExecution() {
        var transition = new StartToUnderReviewTransition();

        var process = OrderProcess.withDefinitionAndState("josé luiz", "novo processo", ProcessState.STARTED);
        var context = new ProcessContext("fábio chagas", process);

        assertDoesNotThrow(() -> transition.onTransition(context));
    }

}
