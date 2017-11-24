package com.searchable.objects.utils.jms;

import org.springframework.beans.factory.annotation.Autowired;
import com.searchable.objects.utils.prop.PropReader;
import javax.annotation.PreDestroy;

/**
 * @auther Archan on 24/11/17.
 */
public abstract class AbstractBuilder<T> implements Builder<T> {
    @Autowired
    protected PropReader propReader;

    @PreDestroy
    public void cleanup(){
        this.destroy();
    }
}
