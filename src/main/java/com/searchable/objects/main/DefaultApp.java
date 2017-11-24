package com.searchable.objects.main;

import com.searchable.objects.core.service.ObjectServiceFacade;
import com.searchable.objects.demo.SearchableEntity;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @auther Archan on 23/11/17.
 */
public class DefaultApp {
    private static Logger logger = LoggerFactory.getLogger(DefaultApp.class);

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ObjectServiceFacade objectServiceFacade = context.getBean(ObjectServiceFacade.class);
        SearchableEntity obj1 = new SearchableEntity(1l, "Test String message1");
        objectServiceFacade.loadObject(obj1);
        SearchableEntity obj2 = new SearchableEntity(2l, "Test String message2");
        objectServiceFacade.loadObject(obj2);

        SearchResult result = objectServiceFacade.search("message1");

        logger.info("Result {} , {}", result, result.getTotal());
    }
}
