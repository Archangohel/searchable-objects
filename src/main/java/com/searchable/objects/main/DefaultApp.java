package com.searchable.objects.main;

import com.google.gson.JsonObject;
import com.searchable.objects.core.service.ObjectServiceFacade;
import com.searchable.objects.demo.DemoClientMethods;
import com.searchable.objects.demo.DemoClientMethods1;
import com.searchable.objects.demo.SearchableEntity;
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

        DemoClientMethods demoClientMethods = context.getBean(DemoClientMethods.class);
//        demoClientMethods.processArgs(new SearchableEntity(111l, "DefaultApp processArgs"));
//        demoClientMethods.processWithoutAdvice(null);
//        demoClientMethods.processReturnValue();
//        demoClientMethods.process(new SearchableEntity(999l, "DefaultApp processArgs"));
        demoClientMethods.deleteMethod(null);

//        DemoClientMethods1 demoClientMethods1 = context.getBean(DemoClientMethods1.class);
//        demoClientMethods1.processArgs(new SearchableEntity(222l, "DefaultApp processArgs"));
//        demoClientMethods1.processWithoutAdvice(null);
//        demoClientMethods1.processReturnValue();
//        demoClientMethods1.process(new SearchableEntity(2229l, "DefaultApp processArgs"));

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ObjectServiceFacade objectServiceFacade = context.getBean(ObjectServiceFacade.class);
        JsonObject searchResult = objectServiceFacade.search("DefaultApp");
        logger.info("Result JSON :: {}", searchResult);
        context.close();
    }
}
