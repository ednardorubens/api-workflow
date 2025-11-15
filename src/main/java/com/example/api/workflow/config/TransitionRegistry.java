package com.example.api.workflow.config;

import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.handler.TransitionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class TransitionRegistry {

    private final Map<ProcessState, Map<ProcessState, TransitionHandler>> transitions = new EnumMap<>(ProcessState.class);

    public void register(ProcessState from, ProcessState to, TransitionHandler handler) {
        transitions.computeIfAbsent(from, k -> new EnumMap<>(ProcessState.class));

        var map = transitions.get(from);
        if (map.containsKey(to)) {
            log.error("Conflito de transição: {} -> {}", from.name(), to.name());
            throw new IllegalStateException("Conflito de transição: " + from.name() + " -> " + to.name());
        }
        log.info("Registrando: {} -> {}", from, to);
        map.put(to, handler);
    }

    Map<ProcessState, Map<ProcessState, TransitionHandler>> getTransitions() {
        return transitions;
    }

    public Optional<TransitionHandler> getHandler(ProcessState from, ProcessState to) {
        return Optional.ofNullable(transitions.getOrDefault(from, Map.of()).get(to));
    }

}
