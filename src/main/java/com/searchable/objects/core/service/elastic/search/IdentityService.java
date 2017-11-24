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

    public String getKeyAsString(Object object) {
        String fieldName = searchableObjectLookupService.getIdFieldForSearchableClass(object.getClass().getCanonicalName());
        if (fieldName != null) {
            Field field = null;
            try {
                field = object.getClass().getDeclaredField(fieldName);
                return field.get(object).toString();
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get the value for entity {} and field {}", object, field, e);
            } catch (IllegalAccessException e) {
                logger.error("Unable to get the value for entity {} and field {}", object, field, e);
            } catch (NoSuchFieldException e) {
                logger.error("field {} not found in object", field, object, e);
            }
        }
        return null;
    }
}
