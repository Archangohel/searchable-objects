package com.searchable.objects.utils.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * @auther Archan on 23/11/17.
 */

@Component
public class QueueBuilder extends AbstractBuilder<SessionAndQueue> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionBuilder sessionBuilder;

    private SessionAndQueue sessionAndQueue;

    @Override
    public SessionAndQueue build() {
        Session session = sessionBuilder.build();
        try {
            Queue queue = session.createQueue(propReader.getProperty("jms.activemq.name"));
            this.sessionAndQueue = new SessionAndQueue(session, queue);
            return this.sessionAndQueue;
        } catch (JMSException e) {
            logger.error("Error in creating the queue the broker!", e);
        }
        return null;
    }

    @Override
    public void destroy() {
        sessionBuilder.destroy();
        sessionAndQueue = null;
    }
}
