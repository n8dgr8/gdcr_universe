package com.pillartechnology.gdcr.universe.domain;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.UNKNOWN;

public class Generation {

    public static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public enum E_CellState {
        ALIVE,
        DEAD,
        UNKNOWN
    }

    private RedisTemplate<String, String> redisTemplate;

    private Map<String, E_CellState> world = new HashMap<>();
    private String generationId;
    private String universeId;

    public Generation(String id, RedisTemplate<String, String> newRedisTemplate) {
        redisTemplate = newRedisTemplate;
        generationId = id;
        world.clear();

        redisTemplate.opsForHash().entries(String.format("generation:%s", id)).forEach((cellKey, cellState) -> {
            world.put(cellKey.toString(), E_CellState.valueOf(cellState.toString()));
        });
    }

    public Generation(Integer width, Integer height, String newUniverseId, RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        generationId = UUID.randomUUID().toString();

        IntStream.range(0, height).forEach(colInt -> {
            IntStream.range(0, width).forEach(rowInt -> {
                String key = getLetterForRowInt(rowInt);
                key = key + String.valueOf(colInt);
                world.put(key, UNKNOWN);
                redisTemplate.opsForHash().put(String.format("generation:%s", generationId), key, UNKNOWN.toString());
            });
        });
    }

    public void setCellState(String cellId, E_CellState cellState) {
        redisTemplate.opsForHash().put(String.format("generation:%s", generationId), cellId, cellState.toString());
        world.put(cellId, cellState);
    }

    public Map<String, E_CellState> getWorld() {
        return world;
    }

    public String getGenerationId() {
        return generationId;
    }

    private String getLetterForRowInt(int rowInt) {
        return Character.toString(ALPHABET[rowInt]);
    }

}
