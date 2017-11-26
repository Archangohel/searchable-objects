package com.searchable.objects.demo;

import java.io.Serializable;
import java.util.Random;

/**
 * @auther Archan on 26/11/17.
 */
public class EntityChild implements Serializable {
    private static Random random = new Random();

    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static EntityChild newInstance(){
        EntityChild child = new EntityChild();
        child.setId(random.nextLong());
        child.setValue("value-"+random.nextInt());
        return child;
    }
}
