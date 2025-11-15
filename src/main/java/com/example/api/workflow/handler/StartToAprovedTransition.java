package com.example.api.workflow.handler;

import com.example.api.workflow.annotation.Transition;
import com.example.api.workflow.enums.ProcessState;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.model.OrderProcess;
import com.example.api.workflow.model.ProcessContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transition(from = ProcessState.STARTED, to = ProcessState.APROVED)
public class StartToAprovedTransition implements TransitionHandler {

    @Override
    public OrderProcess onTransition(ProcessContext processContext) throws TransitionException {
        return processContext.orderProcess();
    }

}
