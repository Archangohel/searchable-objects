package com.searchable.objects.core.service;

import com.searchable.objects.core.service.elastic.search.JestLoadIndexService;
import com.searchable.objects.core.service.elastic.search.JestSearchIndexService;
import com.searchable.objects.core.service.elastic.search.SearchQuery;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class ObjectServiceFacade {
    @Autowired
    private JestSearchIndexService jestSearchIndexService;

    @Autowired
    private JestLoadIndexService jestLoadIndexService;

    public SearchResult search(String queryString) {
        return jestSearchIndexService.search(SearchQuery.getSearchQuery(queryString));
    }

    public <T> void loadObjects(List<T> objects) {
        jestLoadIndexService.saveToIndex(objects);
    }

    public <T> void loadObject(T object) {
        List<T> list = new ArrayList<>();
        list.add(object);
        jestLoadIndexService.saveToIndex(list);
    }
}
