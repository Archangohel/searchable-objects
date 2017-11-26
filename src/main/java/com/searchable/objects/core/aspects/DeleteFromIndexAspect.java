package com.searchable.objects.core.aspects;

import com.searchable.objects.core.annotations.DeleteFromIndex;
import com.searchable.objects.core.annotations.ElementTypes;
import com.searchable.objects.core.service.SearchableObjectLookupService;
import com.searchable.objects.utils.jms.ActiveMqFacade;
import com.searchable.objects.utils.jms.JmsMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.ObjectMessage;
import java.util.Collection;
import java.util.Map;

/**
 * @auther Archan on 25/11/17.
 */
@Aspect
@Component
public class DeleteFromIndexAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActiveMqFacade activeMqFacade;

    @Autowired
    private SearchableObjectLookupService searchableObjectLookupService;

    @Pointcut("@target(typeAnnotation)")
    public void beanAnnotatedWithMonitor(DeleteFromIndex typeAnnotation) {
    }

    @Pointcut("@annotation(methodAnnotation)")
    public void annotationPointCutDefinition(DeleteFromIndex methodAnnotation) {
    }

    @Pointcut("execution(* *(..))")
    public void atExecution() {
    }

    @Around(value = "annotationPointCutDefinition(methodAnnotation) && atExecution()")
    public Object aroundMethodUpdateToIndex(ProceedingJoinPoint proceedingJoinPoint, DeleteFromIndex methodAnnotation) throws Throwable {
        return aroundUpdateToIndex(proceedingJoinPoint, methodAnnotation);
    }

    @Around(value = "beanAnnotatedWithMonitor(typeAnnotation) && atExecution()")
    public Object aroundTypeUpdateToIndex(ProceedingJoinPoint proceedingJoinPoint, DeleteFromIndex typeAnnotation) throws Throwable {
        return aroundUpdateToIndex(proceedingJoinPoint, typeAnnotation);
    }

    private Object aroundUpdateToIndex(ProceedingJoinPoint proceedingJoinPoint, DeleteFromIndex annotation) throws Throwable {
        // pre processing
        boolean processArguments = false;
        boolean processReturnValue = false;
        for (ElementTypes elementTypeToProcess : annotation.elementsToProcess()) {
            if (elementTypeToProcess.equals(ElementTypes.ARGUMENT)) {
                processArguments = true;
            } else if (elementTypeToProcess.equals(ElementTypes.RETURN)) {
                processReturnValue = true;
            }
        }
        if (processArguments) {
            for (Object arg : proceedingJoinPoint.getArgs()) {
                if (arg != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("args {}", arg);
                    }
                    if (Collection.class.isAssignableFrom(arg.getClass())) {
                        Collection<?> collection = Collection.class.cast(arg);
                        collection.stream().forEach(r -> {
                            processObject(r);
                        });
                    } else if (Map.class.isAssignableFrom(arg.getClass())) {
                        Map<?, ?> map = Map.class.cast(arg);
                        map.entrySet().stream().forEach(r -> {
                            processObject(r.getKey());
                            processObject(r.getValue());
                        });
                    } else {
                        processObject(arg);
                    }
                }
            }
        }

        final long startTime1 = System.currentTimeMillis();

        // execute the method
        Object ret = proceedingJoinPoint.proceed();

        //post processing
        if (processReturnValue && ret != null) {
            long startTime2 = 0;
            if (logger.isDebugEnabled()) {
                final long endTime1 = System.currentTimeMillis();
                logger.debug("Before executing method for {}. Time taken to execute the method is {} ms",
                        proceedingJoinPoint.getSignature(), endTime1 - startTime1);
                startTime2 = System.currentTimeMillis();
            }

            processObject(ret);
            if (logger.isDebugEnabled()) {
                final long endTime2 = System.currentTimeMillis();
                logger.debug("After executing the method for {}.  Time taken to execute the security is {} ms",
                        proceedingJoinPoint.getSignature(), endTime2 - startTime2);
            }
        }
        return ret;
    }

    private void processObject(Object object) {
        if (searchableObjectLookupService.isObjectClassSearchable(object.getClass())) {
            ObjectMessage message = activeMqFacade.createMessage(new JmsMessage(object, JmsMessage.ActionType.DELETE));
            try {
                activeMqFacade.getMessageProducer().send(message);
            } catch (Exception e) {
                logger.error("Error in sending the object to JMS for adding to index {}. Ignoring the exception.", object, e);
            }
        }
    }

}
