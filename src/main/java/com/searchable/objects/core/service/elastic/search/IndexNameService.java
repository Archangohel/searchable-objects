package com.searchable.objects.core.service.elastic.search;

import com.searchable.objects.core.service.SearchableObjectLookupService;
import com.searchable.objects.utils.prop.PropReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class IndexNameService {

    @Autowired
    private PropReader propReader;

    private String defaultIndexPrefix;

    @Autowired
    private SearchableObjectLookupService searchableObjectLookupService;

    @PostConstruct
    public void init() {
        defaultIndexPrefix = propReader.getProperty("elastic.cluster.default.index.prefix");
    }

    public String getIndexNameForObject(Object object) {
        if (searchableObjectLookupService.isObjectClassSearchable(object.getClass())) {
            return getIndexNameForClass(object.getClass().getCanonicalName());
        } else {
            return null;
        }
    }

    public String getIndexNameForClass(String className) {
        return defaultIndexPrefix + className.toLowerCase();

    }

}
