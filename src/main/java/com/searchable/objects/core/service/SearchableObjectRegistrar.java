package com.searchable.objects.core.service;

import com.searchable.objects.core.annotations.Searchable;
import com.searchable.objects.core.service.elastic.search.JestLoadIndexService;
import com.searchable.objects.utils.prop.PropReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class SearchableObjectRegistrar {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SearchableObjectLookupService searchableObjectLookupService;

    @Autowired
    private PropReader propReader;

    @Autowired
    private JestLoadIndexService jestLoadIndexService;

    private ClassPathScanningCandidateComponentProvider scanner;
    private List<Class<? extends Annotation>> annotationList = Arrays.asList(new Class[]{Searchable.class});

    @PostConstruct
    public void init() {
        initialiseScanner();
        String packagesToScan = propReader.getProperty("com.searchable.objects.scan.packages");
        List<String> scanList = Arrays.asList(packagesToScan.split(","));
        scanList.stream().forEach(r -> {
            scanPackage(r);
        });
        buildIndexes();
    }

    private void buildIndexes() {
        searchableObjectLookupService.getAllSearchableObjects().stream().forEach(r -> {
            jestLoadIndexService.buildIndexByClassName(r);
        });
    }

    private void initialiseScanner() {
        this.scanner = new ClassPathScanningCandidateComponentProvider(false);
        this.annotationList.stream().forEach(r -> {
            this.scanner.addIncludeFilter(new AnnotationTypeFilter(r));
        });
    }

    private void scanPackage(String packageToScan) {
        for (BeanDefinition beanDefinition : this.scanner.findCandidateComponents(packageToScan)) {
            logger.info("Searchable Bean {}", beanDefinition.getBeanClassName());
            String className = beanDefinition.getBeanClassName();
            try {
                Class<?> clazz = Class.forName(className);
                String idField = clazz.getAnnotation(Searchable.class).idField();
                searchableObjectLookupService.registerObjectClass(className, idField);
            } catch (ClassNotFoundException e) {
                logger.error("Class {} not found", className);
            }
        }
    }
}
