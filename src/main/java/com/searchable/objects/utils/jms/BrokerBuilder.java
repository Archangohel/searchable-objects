package com.searchable.objects.utils.jms;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;


/**
 * @auther Archan on 23/11/17.
 */
@Component
public class BrokerBuilder extends AbstractBuilder<BrokerService> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private BrokerService broker;

    @Override
    public BrokerService build() {
        try {
            this.broker = BrokerFactory.createBroker(new URI(
                    "broker:(tcp://" + propReader.getProperty("jms.activemq.host") +
                            ":" + propReader.getProperty("jms.activemq.port") + ")"));
            return broker;
        } catch (Exception e) {
            logger.error("Error creating the broker!", e);
        }
        return null;
    }

    @Override
    public void destroy() {
        if (this.broker != null) {
            try {
                broker.stop();
            } catch (Exception e) {
                logger.error("Error closing the broker!", e);
            }
            broker = null;
        }
    }
}
