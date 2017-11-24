package com.searchable.objects.utils.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class ConsumerMessageListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        logger.info("JMS Message received {}", objectMessage);
        //TODO: load the object to elastic search.

    }
}
