package com.example.api.workflow.aspect;

import com.example.api.workflow.annotation.Transition;
import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.handler.TransitionHandler;
import com.example.api.workflow.model.ProcessContext;
import com.example.api.workflow.model.ProcessHistory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@Order(300)
public class TransitionHistoryAspect {

    @Around("execution(public com.example.api.workflow.model.OrderProcess com.example.api.workflow.handler.*Transition.onTransition(com.example.api.workflow.model.ProcessContext)) && args(processContext)")
    public void logAndRegisterHistory(ProceedingJoinPoint joinPoint, ProcessContext processContext) throws Throwable {
        var target = (TransitionHandler) joinPoint.getTarget();
        var transitionAnnotation = target.getTransitionAnnotation();

        try {
            joinPoint.proceed();
            registerSuccess(processContext, transitionAnnotation);
        } catch (TransitionException ex) {
            registerFail(processContext, transitionAnnotation, ex);
            throw ex;
        }
    }

    private void registerSuccess(ProcessContext processContext, Transition transition) {
        var history = new ProcessHistory(
                    transition.from(),
                    transition.to(),
                    processContext.claimant());

        processContext.orderProcess().withHistory(history);

        log.info("Transição bem-sucedida: {} -> {} por {} às {}",
                history.from(),
                history.to(),
                history.claimant(),
                history.timestamp());
    }

    private void registerFail(ProcessContext context, Transition transition, TransitionException ex) {
        log.error("Falha na transição {} -> {} por {}: às {}, erro: {}",
                transition.from(),
                transition.to(),
                context.claimant(),
                LocalDateTime.now(),
                ex.getMessage());
    }

}
