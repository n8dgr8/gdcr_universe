package net.n8dgr8.gdcr.universe.domain;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Universe {

    public static final int DEFAULT_WIDTH = 26;
    public static final int DEFAULT_HEIGHT = 26;

    private String universeId;
    private Integer universeWidth = DEFAULT_WIDTH;
    private Integer universeHeight = DEFAULT_HEIGHT;

    private List<Generation> generations = new ArrayList<>();

    private RedisTemplate<String, String> redisTemplate;

    public Universe(RedisTemplate<String, String> newRedisTemplate) {
        redisTemplate = newRedisTemplate;
        universeId = UUID.randomUUID().toString();
    }

    public Universe(String id, RedisTemplate<String, String> newRedisTemplate) {
        redisTemplate = newRedisTemplate;
        universeId = id;

        Map<Object, Object> universeAttributes = redisTemplate.opsForHash().entries(String.format("universe:%s", universeId));

        setUniverseHeight(Integer.valueOf(universeAttributes.get("height").toString()));
        setUniverseWidth(Integer.valueOf(universeAttributes.get("width").toString()));
    }

    public String getUniverseId() {
        return universeId;
    }

    public Integer getUniverseHeight() {
        return universeHeight;
    }

    public void setUniverseHeight(Integer newUniverseHeight) {
        redisTemplate.opsForHash().put(String.format("universe:%s", universeId), "height", String.valueOf(newUniverseHeight));
        universeHeight = newUniverseHeight;
    }

    public Integer getUniverseWidth() {
        return universeWidth;
    }

    public void setUniverseWidth(Integer newUniverseWidth) {
        redisTemplate.opsForHash().put(String.format("universe:%s", universeId), "width", String.valueOf(newUniverseWidth));
        universeWidth = newUniverseWidth;
    }

    public List<Generation> getGenerations() {
        return generations;
    }

    public Integer getUniverseSize() {
        return universeHeight * universeWidth;
    }
}
