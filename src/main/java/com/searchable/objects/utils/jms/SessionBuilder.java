package com.searchable.objects.utils.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @auther Archan on 23/11/17.
 */
@Component
public class SessionBuilder extends AbstractBuilder<Session> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Session session;
    private Connection connection;

    @Override
    public Session build() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://" + propReader.getProperty("jms.activemq.host")
                        + ":" + propReader.getProperty("jms.activemq.port"));
        connectionFactory.setTrustAllPackages(true);
        try {
            this.connection = connectionFactory.createConnection();
            this.session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            connection.start();
            return this.session;
        } catch (JMSException e) {
            logger.error("Error starting the broker!", e);
            destroy();
        }
        return null;
    }

    @Override
    public void destroy() {
        if (this.session != null) {
            try {
                this.session.close();
            } catch (JMSException e) {
                logger.error("Error destroying the session!", e);
            }
            this.session = null;
        }
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (JMSException e) {
                logger.error("Error destroying the connection!", e);
            }
            this.connection = null;
        }
    }
}
