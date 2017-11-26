package com.searchable.objects.main;

import com.google.gson.JsonObject;
import com.searchable.objects.core.service.ObjectServiceFacade;
import com.searchable.objects.demo.ClientCode;
import com.searchable.objects.demo.Entity1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @auther Archan on 23/11/17.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DefaultApp {
    private static Logger logger = LoggerFactory.getLogger(DefaultApp.class);

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ClientCode clientCode = context.getBean(ClientCode.class);

        clientCode.processArgsForUpdate(Entity1.newInstance());
        Entity1 entity1 = clientCode.processArgsAndReturnValueForUpdate(Entity1.newInstance());
        clientCode.processArgsForDelete(entity1);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ObjectServiceFacade objectServiceFacade = context.getBean(ObjectServiceFacade.class);
        JsonObject searchResult = objectServiceFacade.search("val-1928487485");
        logger.info("Result JSON :: {}", searchResult);
        context.close();
    }
}
