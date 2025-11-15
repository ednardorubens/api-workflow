package com.example.api.workflow.config;

import com.example.api.workflow.annotation.Transition;
import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.handler.TransitionHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TransitionScanner {

    private final ApplicationContext context;
    private final TransitionRegistry registry;

    @PostConstruct
    public void registerTransitions() {
        var beans = context.getBeansWithAnnotation(Transition.class);

        beans.values()
                .stream()
                .filter(TransitionHandler.class::isInstance)
                .map(TransitionHandler.class::cast)
                .forEach(this::register);

        showWorkflowAscii();
    }

    private void register(TransitionHandler transitionHandler) {
        var annotation = transitionHandler.getTransitionAnnotation();
        registry.register(annotation.from(), annotation.to(), transitionHandler);
    }

    private void showWorkflowAscii() {
        var transitions = registry.getTransitions();

        log.info("=== Workflow ASCII =======================================");

        for (ProcessState from : ProcessState.values()) {
            var stateTransitions = transitions.getOrDefault(from, Map.of());

            if (stateTransitions.isEmpty()) {
                log.info("{} -> (nenhuma transição)", from.name());
            } else {
                for (var entry : stateTransitions.entrySet()) {
                    ProcessState to = entry.getKey();
                    TransitionHandler handler = entry.getValue();
                    String handleName = handler.getClass().getSimpleName();
                    log.info( "{} -> {} [{}]", from.name(), to.name(), handleName);
                }
            }
        }

        log.info("==========================================================");
    }

}
