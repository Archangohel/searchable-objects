package com.searchable.objects.utils.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * @auther Archan on 23/11/17.
 */
public class SessionAndQueue {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Session session;
    private Queue queue;

    public SessionAndQueue(Session session, Queue queue) {
        this.session = session;
        this.queue = queue;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MessageProducer createProducer() {
        try {
            return getSession().createProducer(getQueue());
        } catch (JMSException e) {
            logger.error("Error in creating the message producer for queue!", e);
        }
        return null;
    }

    public MessageConsumer createConsumer() {
        try {
            return getSession().createConsumer(getQueue());
        } catch (JMSException e) {
            logger.error("Error in creating the message consumer for queue!", e);
        }
        return null;
    }

}
