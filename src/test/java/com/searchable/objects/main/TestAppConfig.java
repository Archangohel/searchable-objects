package com.searchable.objects.main;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @auther Archan on 23/11/17.
 */

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.searchable.objects.*")
@PropertySource(value = {"classpath:searchable-objects-test.properties"})
public class TestAppConfig {
}
