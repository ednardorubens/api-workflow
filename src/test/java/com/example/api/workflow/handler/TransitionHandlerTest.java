package com.example.api.workflow.handler;

import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransitionHandlerTest {

    @Test
    void testGetTransitionAnnotation() {
        var transition = new TransitionHandler() {
            @Override
            public OrderProcess onTransition(ProcessContext processContext) throws TransitionException {
                throw new UnsupportedOperationException("Unimplemented method 'onTransition'");
            }
        };

        var ex = assertThrows(IllegalStateException.class, transition::getTransitionAnnotation);

        assertEquals("A classe com.example.api.workflow.handler.TransitionHandlerTest$1 que estende TransitionHandler deve ter uma anotação @Transition", ex.getMessage());
    }

}
