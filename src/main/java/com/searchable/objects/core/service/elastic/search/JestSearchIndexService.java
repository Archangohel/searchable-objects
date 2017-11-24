package com.searchable.objects.core.service.elastic.search;

import com.searchable.objects.core.service.SearchableObjectLookupService;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther Archan on 24/11/17.
 */
@Component
@DependsOn({"searchableObjectRegistrar", "searchableObjectLookupService"})
public class JestSearchIndexService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JestTemplate jestTemplate;

    @Autowired
    private SearchableObjectLookupService searchableObjectLookupService;

    @Autowired
    private IndexNameService indexNameService;

    private List<String> indicesNames;

    @PostConstruct
    public void init() {
        loadIndices();
    }

    private void loadIndices() {
        indicesNames = new ArrayList<>();
        searchableObjectLookupService.getAllSearchableObjects().stream().forEach(r -> {
            indicesNames.add(indexNameService.getIndexNameForClass(r));
        });
    }

    public SearchResult search(String query) {
        try {
            return jestTemplate.search(indicesNames, query, true);
        } catch (JestResultException e) {
            logger.error("Error searching query {}", query, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Error searching query {}", query, e);
            throw new RuntimeException(e);
        }
    }

}
