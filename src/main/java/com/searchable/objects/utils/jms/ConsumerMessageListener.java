package com.searchable.objects.utils.jms;

import com.searchable.objects.core.service.ObjectServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class ConsumerMessageListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectServiceFacade objectServiceFacade;

    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        logger.info("JMS Message received {}", objectMessage);
        try {
            Object object = objectMessage.getObject();
            boolean status;
            if (object instanceof JmsMessage) {
                JmsMessage jmsMessage = (JmsMessage) object;
                if (jmsMessage.isSaveAction()) {
                    status = objectServiceFacade.loadObject(jmsMessage.getObject());
                } else if (jmsMessage.isDeleteAction()) {
                    status = objectServiceFacade.deleteObject(jmsMessage.getObject());
                } else {
                    status = false;
                    logger.warn("Invalid jms message action type [{}] found."
                            , jmsMessage.getActionType());
                }
            } else {
                status = false;
                logger.warn("Invalid jms message type [{}] found, supported message type is"
                        , object.getClass(), JmsMessage.class);
            }
            if (!status) {
                logger.error("Unable to load the object {} to elastic search cluster.", object);
            }
        } catch (JMSException e) {
            logger.error("Error getting the object from JMS message", message);
        }
    }
}
