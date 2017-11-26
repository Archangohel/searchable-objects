package com.searchable.objects.demo;

import com.searchable.objects.core.annotations.Searchable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @auther Archan on 26/11/17.
 */
@Searchable(idField = "id")
public class Entity1 implements Serializable {
    private static Random random = new Random();

    private Long id;
    private String name;
    private EntityChild child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityChild getChild() {
        return child;
    }

    public void setChild(EntityChild child) {
        this.child = child;
    }

    public static Entity1 newInstance() {
        Entity1 entity1 = new Entity1();
        entity1.setId(random.nextLong());
        entity1.setName("TestName" + random.nextInt());
        entity1.setChild(EntityChild.newInstance());
        return entity1;
    }
}
