package com.searchable.objects.core.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class SearchableObjectLookupService {

    private Map<String, String> searchableObjectLookupMap = new ConcurrentHashMap<>();

    public void registerObjectClass(String clazz, String idField) {
        searchableObjectLookupMap.put(clazz, idField);
    }

    public boolean isObjectClassSearchable(String clazz) {
        if (searchableObjectLookupMap.containsKey(clazz)) {
            return true;
        } else {
            return false;
        }
    }

    public String getIdFieldForSearchableClass(String clazz) {
        if (isObjectClassSearchable(clazz)) {
            return searchableObjectLookupMap.get(clazz);
        }
        return null;
    }

    public boolean isObjectClassSearchable(Class<?> clazz) {
        return isObjectClassSearchable(clazz.getCanonicalName());
    }

    public String getIdFieldForSearchableClass(Class<?> clazz) {
        return getIdFieldForSearchableClass(clazz.getCanonicalName());
    }

    public Set<String> getAllSearchableObjects() {
        return searchableObjectLookupMap.keySet();
    }
}
