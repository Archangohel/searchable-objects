package com.searchable.objects.core.service;

import com.searchable.objects.core.service.elastic.search.JestSearchIndexService;
import io.searchbox.core.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class ObjectSearchService {
    @Autowired
    private JestSearchIndexService jestSearchIndexService;

    public SearchResult search(String query) {
        return jestSearchIndexService.search(query);
    }
}
