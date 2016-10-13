package com.pillartechnology.gdcr.universe;

import com.pillartechnology.gdcr.universe.domain.Universe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UniverseApplicationTests {

	@InjectMocks
	private UniverseApplication universeApplicationUt = new UniverseApplication();

	@Mock
	private RedisTemplate template = mock(RedisTemplate.class);

    private ListOperations listOperationsMock = mock(ListOperations.class);
    private List<String> universeGuidList = new ArrayList<>();

    @Before
    public void setUp() {
        Universe universeOne = new Universe();
        Universe universeTwo = new Universe();

        universeGuidList.add(universeOne.getUniverseId());
        universeGuidList.add(universeTwo.getUniverseId());

        when(listOperationsMock.range("universes", 0, -1)).thenReturn(universeGuidList);
        when(template.opsForList()).thenReturn(listOperationsMock);
    }

	@Test
	public void contextLoads() {
	}

	@Test
	public void getUniversesReturnsAMapOfUniverses() {
		Map<String, Universe> actualKnownUniverses = universeApplicationUt.getKnownUniverses();
        assertThat(actualKnownUniverses.size(), is(universeGuidList.size()));
	}

}
