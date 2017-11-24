package com.searchable.objects.utils.jms;

/**
 * @auther Archan on 23/11/17.
 */
public interface Builder<T> {
    T build();

    void destroy();
}
