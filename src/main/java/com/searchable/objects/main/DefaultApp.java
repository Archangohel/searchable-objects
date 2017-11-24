package com.searchable.objects.main;

import com.searchable.objects.utils.jms.ActiveMqFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

/**
 * @auther Archan on 23/11/17.
 */
public class DefaultApp {
    private static Logger logger = LoggerFactory.getLogger(DefaultApp.class);

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
