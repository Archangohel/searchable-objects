package com.searchable.objects.utils.jms;

import com.searchable.objects.utils.prop.PropReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @auther Archan on 25/11/17.
 */
@Component
public class JmsInitializerAndDestroyerListener implements ApplicationListener<ApplicationContextEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Object lock = new Object();

    @Autowired
    private PropReader propReader;

    @Autowired
    private ActiveMqFacade activeMqFacade;

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        logger.debug("ApplicationContextEvent event {} fired!", event);

        if (event instanceof ContextRefreshedEvent) {
            logger.info("ContextRefreshedEvent event fired!");
            synchronized (lock) {
                String autoStartupSetting = propReader.getProperty("jms.activemq.auto.initialize.on.startup");
                if ("true".equalsIgnoreCase(autoStartupSetting)) {
                    logger.info("Auto initialization enabled. Loading the jms!");
                    activeMqFacade.initialize();
                }
            }
        } else if (event instanceof ContextClosedEvent || event instanceof ContextStoppedEvent) {
            logger.info("ContextClosedEvent/ContextStoppedEvent event fired! Destroying the jms!");
            synchronized (lock) {
                activeMqFacade.destroyQueue();
            }
        }
    }
}
