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
    private Map<String, String> valueMap;

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

    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public static Entity1 newInstance() {
        Entity1 entity1 = new Entity1();
        entity1.setId(random.nextLong());
        entity1.setName("TestName" + random.nextInt());
        Map<String, String> map = new HashMap<>();
        final int maxLoopCount = random.nextInt(10);
        for (int i = 0; i < maxLoopCount; i++) {
            map.put(random.nextInt() + "", "val-" + random.nextInt());
        }
        entity1.setValueMap(map);
        return entity1;
    }
}
