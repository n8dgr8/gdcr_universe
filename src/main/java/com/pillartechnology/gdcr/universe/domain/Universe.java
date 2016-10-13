package com.pillartechnology.gdcr.universe.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Universe {

    public static final int DEFAULT_WIDTH = 26;
    public static final int DEFAULT_HEIGHT = 26;

    @Autowired
    private RedisTemplate<Object, Object> template;

    private String universeId;
    private Integer universeWidth = DEFAULT_WIDTH;
    private Integer universeHeight = DEFAULT_HEIGHT;

    private List<Generation> generations = new ArrayList<>();

    public Universe() {
        universeId = UUID.randomUUID().toString();
    }

    public Universe(String id) {
        universeId = id;
    }

    public String getUniverseId() {
        return universeId;
    }

    public Integer getUniverseHeight() {
        return universeHeight;
    }

    public void setUniverseHeight(Integer newUniverseHeight) {
        universeHeight = newUniverseHeight;
    }

    public Integer getUniverseWidth() {
        return universeWidth;
    }

    public void setUniverseWidth(Integer newUniverseWidth) {
        universeWidth = newUniverseWidth;
    }

    public List<Generation> getGenerations() {
        template.hasKey("goof");
        return generations;
    }

    public Boolean doesKeyExist(String bleh) {
        System.err.println("Looking for " + bleh);
        return template.hasKey(bleh);
    }

    public Integer getUniverseSize() {
        return universeHeight * universeWidth;
    }
}
