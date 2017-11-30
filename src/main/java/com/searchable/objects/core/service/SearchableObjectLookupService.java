package com.searchable.objects.core.service;

import com.searchable.objects.utils.prop.PropReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class SearchableObjectLookupService {

    @Autowired
    private PropReader propReader;

    private Map<String, String> searchableObjectLookupMap = new ConcurrentHashMap<>();
    private Set<String> objectClassMethods = new HashSet<>();

    @PostConstruct
    public void init() {
        String methods = propReader.getProperty("com.searchable.objects.identification.method");
        for (String methodName : methods.split(",")) {
            objectClassMethods.add(methodName);
        }
    }

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

    public Object convert(Object object) {
        for (String methodName : objectClassMethods) {
            try {
                Method method = object.getClass().getMethod(methodName);
                if (method != null) {
                    Object object1 = method.invoke(object);
                    if (object1 != null) {
                        return object1;
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public boolean isObjectSearchable(Object object) {
        return isObjectClassSearchable(object.getClass().getCanonicalName());
    }

    public String getIdFieldForSearchableClass(Class<?> clazz) {
        return getIdFieldForSearchableClass(clazz.getCanonicalName());
    }

    public Set<String> getAllSearchableObjects() {
        return searchableObjectLookupMap.keySet();
    }
}
