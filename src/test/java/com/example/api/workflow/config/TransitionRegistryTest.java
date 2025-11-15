package com.example.api.workflow.config;

import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.handler.StartToAprovedTransition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransitionRegistryTest {

    @Test
    void testRegisterSearch() {
        var registry = new TransitionRegistry();
        var handler = new StartToAprovedTransition();

        registry.register(ProcessState.STARTED, ProcessState.UNDER_REVIEW, handler);

        assertEquals(handler, registry.getHandler(ProcessState.STARTED, ProcessState.UNDER_REVIEW).get());
    }

    @Test
    void testRegisterAndConflictDetection() {
        var registry = new TransitionRegistry();
        var handler = new StartToAprovedTransition();

        registry.register(ProcessState.STARTED, ProcessState.UNDER_REVIEW, handler);

        // Deve lançar exceção ao registrar mesma transição
        var exception = assertThrows(
            IllegalStateException.class,
            () -> registry.register(ProcessState.STARTED, ProcessState.UNDER_REVIEW, handler)
        );

        assertEquals("Conflito de transição: STARTED -> UNDER_REVIEW", exception.getMessage());
    }

}
