package com.searchable.objects.core.service.elastic.search;

import com.searchable.objects.core.service.SearchableObjectLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @auther Archan on 24/11/17.
 */
@Component
public class IdentityService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SearchableObjectLookupService searchableObjectLookupService;

    public String getKeyAsString(Object object, Class<?> clazz) {
        String fieldName = searchableObjectLookupService.getIdFieldForSearchableClass(object.getClass().getCanonicalName());
        if (fieldName != null) {
            Field field = null;
            try {
                if (clazz == null) {
                    field = object.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.get(object).toString();
                } else {
                    field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.get(object).toString();
                }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get the value for entity {} and field {}", object, field, e);
            } catch (IllegalAccessException e) {
                logger.error("Unable to get the value for entity {} and field {}", object, field, e);
            } catch (NoSuchFieldException e) {
                Class<?> superClass;
                if (clazz == null) {
                    superClass = object.getClass().getSuperclass();
                } else {
                    superClass = clazz.getSuperclass();
                }
                if (superClass != null) {
                    return getKeyAsString(object, superClass);
                }
                logger.error("field {} not found in object", field, object, e);
            }
        }
        return null;
    }
}
