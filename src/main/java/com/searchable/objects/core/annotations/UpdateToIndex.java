package com.searchable.objects.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Should be applied any spring bean method that has an impact (create new or update) on any
 * searchable entity(Class having {@link Searchable} annotation).
 *
 * @auther Archan on 23/11/17.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateToIndex {
    ElementTypes[] elementsToProcess() default {ElementTypes.ARGUMENT};

    Class[] skipObjectTypes() default {};
}
