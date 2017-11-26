package com.searchable.objects.core.service.elastic.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class JestDeleteIndexService {
    @Autowired
    private JestTemplate jestTemplate;

    @Autowired
    private IndexNameService indexNameService;

    public <T> boolean deleteFromIndex(List<T> objects) {
        Assert.notEmpty(objects, "Objects cant be empty");
        String indexName = indexNameService.getIndexNameForObject(objects.get(0));
        String simpleName = objects.get(0).getClass().getSimpleName();
        if (indexName != null) {
            return jestTemplate.removeFromIndex(indexName, simpleName, objects);
        }
        return false;
    }

    public void deleteIndex(String indexName) {
        if (jestTemplate.isExists(indexName)) {
            jestTemplate.deleteIndex(indexName);
        }
    }

}
