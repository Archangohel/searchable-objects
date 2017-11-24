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
public class JmsTesterApp {
    private static Logger logger = LoggerFactory.getLogger(JmsTesterApp.class);

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ActiveMqFacade activeMqFacade = context.getBean(ActiveMqFacade.class);

        // create the jms queue
        activeMqFacade.initialize();

        MessageProducer producer = activeMqFacade.getMessageProducer();

        // send message
        TestMessage messageObject = new TestMessage(1l, "Test String message");
        ObjectMessage msg = activeMqFacade.createMessage(messageObject);
        try {
            producer.send(msg);
        } catch (Exception e) {
            logger.error("Error in sending the message");
            activeMqFacade.destroyQueue();
            System.exit(0);
        }

        //receive message
        MessageConsumer consumer = activeMqFacade.getMessageConsumer();

        try {
            ObjectMessage receivedMessage = (ObjectMessage) consumer.receive();
            if (receivedMessage.getObject().equals(messageObject)) {
                logger.info("Test Passed");
            }
        } catch (Exception e) {
            logger.error("Error in receiving the message", e);
        }

        // cleanup
        activeMqFacade.destroyQueue();
    }
}
