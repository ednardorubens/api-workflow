package com.example.api.workflow.aspect;

import com.example.api.workflow.handler.TransitionHandler;
import com.example.api.workflow.model.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(200)
public class TransitionStatusAspect {

    @Around("execution(public com.example.api.workflow.model.OrderProcess com.example.api.workflow.handler.*Transition.onTransition(com.example.api.workflow.model.ProcessContext)) && args(processContext)")
    public Object updateStatus(ProceedingJoinPoint joinPoint, ProcessContext processContext) throws Throwable {
        var orderProcess = processContext.orderProcess();
        var target = (TransitionHandler) joinPoint.getTarget();
        var annotation = target.getTransitionAnnotation();
        log.info("Atualizando status no método [{}] para o processo {}", joinPoint.getSignature(), orderProcess.id());
        processContext.withState(annotation.to());
        return joinPoint.proceed();
    }

}
