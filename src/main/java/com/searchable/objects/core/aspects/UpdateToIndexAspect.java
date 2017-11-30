package com.searchable.objects.core.aspects;

import com.searchable.objects.core.service.SearchableObjectLookupService;
import com.searchable.objects.utils.jms.ActiveMqFacade;
import com.searchable.objects.utils.jms.JmsMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
public class UpdateToIndexAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActiveMqFacade activeMqFacade;

    @Autowired
    private SearchableObjectLookupService searchableObjectLookupService;

    @Around("@annotation(com.searchable.objects.core.annotations.UpdateToIndexBeforeExecution)")
    public Object aroundMethodUpdateToIndex(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return aroundUpdateToIndex(proceedingJoinPoint, true, false);
    }

    @Around("@annotation(com.searchable.objects.core.annotations.UpdateToIndexAfterExecution)")
    public Object aroundTypeUpdateToIndex(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return aroundUpdateToIndex(proceedingJoinPoint, false, true);
    }

    private Object aroundUpdateToIndex(ProceedingJoinPoint proceedingJoinPoint, boolean processArguments, boolean processReturnValue) throws Throwable {
        // pre processing

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
        Object objectFeedJms = searchableObjectLookupService.convert(object);
        if (searchableObjectLookupService.isObjectSearchable(objectFeedJms)) {
            try {
                ObjectMessage message = activeMqFacade.createMessage(new JmsMessage(objectFeedJms, JmsMessage.ActionType.SAVE));
                activeMqFacade.getMessageProducer().send(message);
            } catch (Exception e) {
                logger.error("Error in sending the object to JMS for adding to index {}. Ignoring the exception.", object, e);
            }
        }
    }

}
