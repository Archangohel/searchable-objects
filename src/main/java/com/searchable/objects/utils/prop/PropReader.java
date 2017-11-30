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
    private String propertyFileOverrides = "searchable-objects-overrides.properties";

    private Properties properties;

    @PostConstruct
    public void init() {
        this.properties = new Properties();
        loadDefault();
        loadOverrides();
    }

    private void loadOverrides() {
        loadPropertyFile(propertyFileOverrides);
    }

    private void loadDefault() {
        loadPropertyFile(propertyFile);
    }

    private void loadPropertyFile(String classPathPropertyFile) {
        ClassPathResource resource = new ClassPathResource(classPathPropertyFile);
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
