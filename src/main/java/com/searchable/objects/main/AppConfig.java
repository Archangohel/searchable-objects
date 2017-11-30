package com.searchable.objects.main;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @auther Archan on 23/11/17.
 */

@Configuration
@ComponentScan(basePackages = "com.searchable.objects.*")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySources({
        @PropertySource(name="defaultProperties", value = {"classpath:searchable-objects.properties"}),
        @PropertySource(name = "overrideProperties", value = "classpath:searchable-objects-overrides.properties", ignoreResourceNotFound = true)
})
public class AppConfig {
}
