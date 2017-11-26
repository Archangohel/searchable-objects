package com.searchable.objects.core.service;

import com.google.gson.JsonObject;
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

    public enum ResultType {
        OBJECT(SearchResult.class), JSON(JsonObject.class);
        private Class<?> classType;

        ResultType(Class<?> classType) {
            this.classType = classType;
        }

    }

    public Object search(String queryString) {
        return search(queryString, ResultType.JSON);
    }

    public Object search(String queryString, ResultType resultType) {
        SearchResult resultObject = jestSearchIndexService.search(SearchQuery.getSearchQuery(queryString));
        if (ResultType.JSON.equals(resultType)) {
            return resultObject.getJsonObject();
        } else if (ResultType.OBJECT.equals(resultType)) {
            return resultObject;
        } else {
            return null;
        }
    }

    public <T> boolean loadObjects(List<T> objects) {
        return jestLoadIndexService.saveToIndex(objects);
    }

    public <T> boolean loadObject(T object) {
        List<T> list = new ArrayList<>();
        list.add(object);
        return jestLoadIndexService.saveToIndex(list);
    }

    public <T> boolean deleteObjects(List<T> objects) {
        return false;
    }

    public <T> boolean deleteObject(T object) {
        return false;
    }
}
