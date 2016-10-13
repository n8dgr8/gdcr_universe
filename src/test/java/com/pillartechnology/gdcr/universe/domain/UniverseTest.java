package com.pillartechnology.gdcr.universe.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//@RunWith(MockitoJUnitRunner.class)
public class UniverseTest {

    public static final int DEFAULT_WIDTH = 26;
    public static final int DEFAULT_HEIGHT = 26;
    public static final int SOME_HEIGHT = 5;
    public static final int SOME_WIDTH = 20;

//    @InjectMocks
    private Universe universeUt = new Universe();

    @Mock
    private RedisTemplate template = mock(RedisTemplate.class);

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

}
