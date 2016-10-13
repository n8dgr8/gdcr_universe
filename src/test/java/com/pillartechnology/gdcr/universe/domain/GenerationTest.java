package com.pillartechnology.gdcr.universe.domain;

import com.pillartechnology.gdcr.universe.UniverseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;

import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.ALIVE;
import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.DEAD;
import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.UNKNOWN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GenerationTest {

    @InjectMocks
    private Generation generationUt = new Generation(2, 2);

    @Mock
    private RedisTemplate template = mock(RedisTemplate.class);

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
        generationUt.save();

        Generation generationById = new Generation(generationUt.getGenerationId());
    }
}
