package com.searchable.objects.main;

/**
 * @auther Archan on 23/11/17.
 */
public class JmsTesterApp {
    /*private static Logger logger = LoggerFactory.getLogger(JmsTesterApp.class);

    public static void main(String args[]) {
        boolean testConsumerListener = true;
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ActiveMqFacade activeMqFacade = context.getBean(ActiveMqFacade.class);

        // create the jms queue
        activeMqFacade.initialize();

        MessageProducer producer = activeMqFacade.getMessageProducer();

        // send message
        Entity1 messageObject = Entity1.newInstance();
        ObjectMessage msg = activeMqFacade.createMessage(messageObject);
        try {
            producer.send(msg);
        } catch (Exception e) {
            logger.error("Error in sending the message");
            activeMqFacade.destroyQueue();
            System.exit(0);
        }

        //receive message
        if (!testConsumerListener) {
            MessageConsumer consumer = activeMqFacade.getMessageConsumer();
            try {
                ObjectMessage receivedMessage = (ObjectMessage) consumer.receive();
                if (receivedMessage.getObject().equals(messageObject)) {
                    logger.info("Test Passed");
                }
            } catch (Exception e) {
                logger.error("Error in receiving the message", e);
            }

        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // cleanup
        activeMqFacade.destroyQueue();
    }*/
}
