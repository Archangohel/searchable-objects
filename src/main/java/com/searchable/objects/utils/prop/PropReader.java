package com.searchable.objects.utils.prop;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class PropReader {

    private String propertyFile = "searchable-objects.properties";
    private Properties properties;

    @PostConstruct
    public void init() {
        ClassPathResource resource = new ClassPathResource(propertyFile);
        this.properties = new Properties();
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        init();
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }
}
