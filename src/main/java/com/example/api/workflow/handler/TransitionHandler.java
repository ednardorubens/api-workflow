package com.example.api.workflow.handler;

import com.example.api.workflow.annotation.Transition;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;

import java.util.Optional;

public interface TransitionHandler {

    OrderProcess onTransition(ProcessContext processContext) throws TransitionException;

    default Transition getTransitionAnnotation() {
        var annotation = this.getClass().getAnnotation(Transition.class);
        return Optional.ofNullable(annotation)
                .orElseThrow(() -> new IllegalStateException(
                    "A classe %s que estende TransitionHandler deve ter uma anotação @Transition".formatted(this.getClass().getName())
                ));
    }

}
