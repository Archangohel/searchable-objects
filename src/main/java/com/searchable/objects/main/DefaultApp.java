package com.searchable.objects.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther Archan on 23/11/17.
 */
public class DefaultApp {
    private static Logger logger = LoggerFactory.getLogger(DefaultApp.class);

    /*public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ClientCode clientCode = context.getBean(ClientCode.class);

        clientCode.processArgsForUpdate(Entity1.newInstance());
        Entity1 entity1 = clientCode.processArgsAndReturnValueForUpdate(Entity1.newInstance());
        //clientCode.processArgsForDelete(entity1);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ObjectServiceFacade objectServiceFacade = context.getBean(ObjectServiceFacade.class);
        JsonObject searchResult = objectServiceFacade.search("TestName621161277");
        logger.info("Result JSON :: {}", searchResult);
        context.close();
    }*/
}
