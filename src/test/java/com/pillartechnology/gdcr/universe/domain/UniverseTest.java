package com.pillartechnology.gdcr.universe.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UniverseTest {

    public static final int DEFAULT_WIDTH = 26;
    public static final int NON_DEFAULT_WIDTH = 6;
    public static final int DEFAULT_HEIGHT = 26;
    public static final int NON_DEFAULT_HEIGHT = 5;
    public static final int SOME_HEIGHT = 5;
    public static final int SOME_WIDTH = 20;

    private RedisTemplate templateMock = mock(RedisTemplate.class);
    private HashOperations operationsMock = mock(HashOperations.class);
    private Universe universeUt;

    @Before
    public void setUp() {
        universeUt = new Universe(templateMock);
        when(templateMock.opsForHash()).thenReturn(operationsMock);

        Map<String, String> universeAttributes = new HashMap<>();
        universeAttributes.put("width", String.valueOf(NON_DEFAULT_WIDTH));
        universeAttributes.put("height", String.valueOf(NON_DEFAULT_HEIGHT));

        String universeAttributesKey = String.format("universe:" + universeUt.getUniverseId());

        when(operationsMock.entries(universeAttributesKey)).thenReturn(universeAttributes);
    }

    @Test
    public void universeHasSomeAttributes() {
        assertThat(universeUt.getUniverseId(), is(instanceOf(String.class)));
    }

    @Test
    public void universeHasADefaultWidth() {
        assertThat(universeUt.getUniverseWidth(), is(DEFAULT_WIDTH));
    }

    @Test
    public void universeHasADefaultHeight() {
        assertThat(universeUt.getUniverseHeight(), is(DEFAULT_HEIGHT));
    }

    @Test
    public void universeCanCalculateTotalSize() {
        universeUt.setUniverseHeight(SOME_HEIGHT);
        universeUt.setUniverseWidth(SOME_WIDTH);

        assertThat(universeUt.getUniverseSize(), is(SOME_HEIGHT * SOME_WIDTH));
    }

    @Test
    public void universeCanBeRetrievedById() {
        Universe universeById = new Universe(universeUt.getUniverseId(), templateMock);
        assertThat(universeById.getUniverseHeight(), is(NON_DEFAULT_HEIGHT));
        assertThat(universeById.getUniverseWidth(), is(NON_DEFAULT_WIDTH));
    }

}
