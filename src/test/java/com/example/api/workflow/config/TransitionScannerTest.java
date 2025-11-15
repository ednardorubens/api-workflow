package com.example.api.workflow.config;

import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.handler.StartToUnderReviewTransition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TransitionScannerTest {

    @Test
    void testWorkflowRegistration() {
        try (var ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(TransitionRegistry.class);
            ctx.register(TransitionScanner.class);
            ctx.register(StartToUnderReviewTransition.class);
            ctx.refresh();

            var registry = ctx.getBean(TransitionRegistry.class);
            assertTrue(registry.getTransitions().containsKey(ProcessState.STARTED));
            assertTrue(registry.getTransitions().get(ProcessState.STARTED).containsKey(ProcessState.UNDER_REVIEW));
        }
    }

}