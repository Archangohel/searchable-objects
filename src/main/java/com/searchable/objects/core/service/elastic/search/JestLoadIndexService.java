package com.searchable.objects.core.service.elastic.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class JestLoadIndexService {
    @Autowired
    private JestTemplate jestTemplate;

    @Autowired
    private IndexNameService indexNameService;

    public <T> boolean saveToIndex(List<T> objects) {
        Assert.notEmpty(objects, "Objects cant be empty");
        String indexName = indexNameService.getIndexNameForObject(objects.get(0));
        String simpleName = objects.get(0).getClass().getSimpleName();
        if (indexName != null) {
            return jestTemplate.addToIndex(indexName, simpleName, objects);
        }
        return false;
    }

    public void buildIndex(String indexName) {
        if (!jestTemplate.isExists(indexName)) {
            jestTemplate.createIndex(indexName);
        }
    }

    public void buildIndexByClassName(String className) {
        String indexName = indexNameService.getIndexNameForClass(className);
        buildIndex(indexName);
    }
}
