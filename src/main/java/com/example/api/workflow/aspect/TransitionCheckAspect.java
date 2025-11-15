package com.example.api.workflow.aspect;

import com.example.api.workflow.exception.TransitionException;
import com.example.api.workflow.handler.TransitionHandler;
import com.example.api.workflow.model.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@Order(100)
public class TransitionCheckAspect {

    @Around("execution(public com.example.api.workflow.model.OrderProcess com.example.api.workflow.handler.*Transition.onTransition(com.example.api.workflow.model.ProcessContext)) && args(processContext)")
    public Object checkStatus(ProceedingJoinPoint joinPoint, ProcessContext processContext) throws Throwable {
        var orderProcess = processContext.orderProcess();
        log.info("Verificando pré-requisitos no método [{}] para o processo {}", joinPoint.getSignature(), orderProcess.id());
        var target = (TransitionHandler) joinPoint.getTarget();
        var annotation = target.getTransitionAnnotation();
        if (!Objects.equals(annotation.from(), orderProcess.state())) {
            var error = "O processo %s tem status(%s) diferente do status(%s) requerido".formatted(
                orderProcess.id(), orderProcess.state(), annotation.from());
            log.error(error);
            throw new TransitionException(error);
        }
        return joinPoint.proceed();
    }

}
