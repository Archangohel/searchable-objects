package com.searchable.objects.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark the object as a searchable object for the framework.
 * Typically the object should be the JPA entity {@link javax.persistence.Entity}.
 *
 * @auther Archan on 23/11/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Searchable{
}
