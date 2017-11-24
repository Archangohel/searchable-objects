package com.searchable.objects.utils.jms;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * @auther Archan on 23/11/17.
 */
@Component
public class ActiveMqFacade {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BrokerBuilder brokerBuilder;

    @Autowired
    private QueueBuilder queueBuilder;

    @Autowired
    private ConsumerMessageListener consumerMessageListener;

    private SessionAndQueue jmsSessionAndQueue;

    private MessageProducer messageProducer;

    private MessageConsumer messageConsumer;

    private volatile boolean initialized = false;

    public synchronized void initialize() {
        if (!initialized) {
            BrokerService broker = brokerBuilder.build();
            try {
                broker.start();
                this.jmsSessionAndQueue = queueBuilder.build();
                this.messageProducer = jmsSessionAndQueue.createProducer();
                this.messageConsumer = jmsSessionAndQueue.createConsumer();
                this.messageConsumer.setMessageListener(consumerMessageListener);
                initialized = true;
            } catch (Exception e) {
                logger.error("Error starting the broker!");
                destroyQueue();
            }
        }
    }

    public void destroyQueue() {
        brokerBuilder.destroy();
        queueBuilder.destroy();
        destroyConsumer();
        destroyProducer();
    }

    private void destroyConsumer() {
        if (this.getMessageConsumer() != null) {
            try {
                this.getMessageConsumer().close();
            } catch (JMSException e) {
                logger.error("Error in closing the MessageConsumer!", e);
            }
        }
    }

    private void destroyProducer() {
        if (this.getMessageProducer() != null) {
            try {
                this.getMessageProducer().close();
            } catch (JMSException e) {
                logger.error("Error in closing the MessageProducer!", e);
            }
        }
    }

    public MessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public <T extends Serializable> ObjectMessage createMessage(T object) {
        try {
            return this.jmsSessionAndQueue.getSession().createObjectMessage(object);
        } catch (JMSException e) {
            logger.error("Error in creating message!", e);
        }
        return null;
    }
}
