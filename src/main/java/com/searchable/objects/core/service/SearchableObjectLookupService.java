package com.searchable.objects.core.service;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class SearchableObjectLookupService {

    private Set<String> searchableObjectLookupSet = new HashSet<>();

    public void registerObjectClass(String clazz) {
        searchableObjectLookupSet.add(clazz);
    }

    public boolean isObjectClassSearchable(String clazz) {
        if (searchableObjectLookupSet.contains(clazz)) {
            return true;
        } else {
            return false;
        }
    }
}
