package com.pillartechnology.gdcr.universe.domain;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.ALIVE;
import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.DEAD;
import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.UNKNOWN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerationTest {

    private RedisTemplate templateMock = mock(RedisTemplate.class);
    private Generation generationUt;
    private Generation generationByIdUt;

    private HashOperations hashOperationsMock = mock(HashOperations.class);
    private Map<String, String> fakeWorld = new HashMap<>();

    @Before
    public void setUp() {
        when(templateMock.opsForHash()).thenReturn(hashOperationsMock);

        generationUt = new Generation(2, 2, templateMock);

        String generationUtKey = String.format("generation:%s", generationUt.getGenerationId());

        fakeWorld.put("A0", UNKNOWN.toString());
        fakeWorld.put("A1", UNKNOWN.toString());
        fakeWorld.put("B0", ALIVE.toString());
        fakeWorld.put("B1", DEAD.toString());

        when(hashOperationsMock.entries(generationUtKey)).thenReturn(fakeWorld);

        generationByIdUt = new Generation(generationUt.getGenerationId(), templateMock);
    }

    @Test
    public void generationCreatesAWorldWithCoordinates() {
        assertThat(generationUt.getWorld().size(), is(4));
        assertThat(generationUt.getWorld().containsKey("A0"), is(true));
        assertThat(generationUt.getWorld().containsKey("A1"), is(true));
        assertThat(generationUt.getWorld().containsKey("B0"), is(true));
        assertThat(generationUt.getWorld().containsKey("B1"), is(true));
    }

    @Test
    public void generationCreatesAWorldWithUnknownStates() {
        assertThat(generationUt.getWorld().containsValue(UNKNOWN), is(true));
        assertThat(generationUt.getWorld().containsValue(ALIVE), is(false));
        assertThat(generationUt.getWorld().containsValue(DEAD), is(false));
    }

    @Test
    public void canGetExistingGenerationById() {
        assertThat(generationByIdUt.getWorld().size(), is(fakeWorld.size()));
    }
}
