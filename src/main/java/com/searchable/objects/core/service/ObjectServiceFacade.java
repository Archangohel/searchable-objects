package com.searchable.objects.core.service;

import com.google.gson.JsonObject;
import com.searchable.objects.core.service.elastic.search.JestDeleteIndexService;
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

    @Autowired
    private JestDeleteIndexService jestDeleteIndexService;

    public enum ResultType {
        OBJECT(SearchResult.class), JSON(JsonObject.class);
        private Class<?> classType;

        ResultType(Class<?> classType) {
            this.classType = classType;
        }

        public Class<?> getClassType() {
            return classType;
        }
    }

    public JsonObject search(String queryString) {
        return search(queryString, JsonObject.class);
    }

    public <T> T search(String queryString, Class<T> clazz) {
        SearchResult resultObject = jestSearchIndexService.search(SearchQuery.getSearchQuery(queryString));
        if (JsonObject.class.isAssignableFrom(clazz)) {
            return (T) resultObject.getJsonObject();
        } else if (SearchResult.class.isAssignableFrom(clazz)) {
            return (T) resultObject;
        } else {
            throw new IllegalArgumentException("Return type {} not supported");
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
        return jestDeleteIndexService.deleteFromIndex(objects);
    }

    public <T> boolean deleteObject(T object) {
        List<T> list = new ArrayList<>();
        list.add(object);
        return jestDeleteIndexService.deleteFromIndex(list);
    }

}
