package com.pillartechnology.gdcr.universe.controllers;

import com.pillartechnology.gdcr.universe.UniverseApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UniverseControllerTest {
    public static final String NON_DEFAULT_WIDTH = "6";
    public static final String NON_DEFAULT_HEIGHT = "5";

    private UniverseApplication universeApplicationMock = mock(UniverseApplication.class);

    @InjectMocks
    private UniverseController universeControllerUt = new UniverseController(universeApplicationMock);

    private List<String> fakeUniverseIds = new ArrayList<>();
    private Model modelMock = mock(Model.class);

    @Mock
    private RedisTemplate templateMock = mock(RedisTemplate.class);
    private HashOperations hashOperationsMock = mock(HashOperations.class);

    @Before
    public void setUp() {
        fakeUniverseIds.add("abc123");
        fakeUniverseIds.add("def456");
    }

    @Test
    public void universeControllerSetsModelWithKnownUniverses() {
        when(universeApplicationMock.getKnownUniverseIds()).thenReturn(fakeUniverseIds);

        String response = universeControllerUt.getUniverses(modelMock);

        assertThat(response, is("universes"));

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> modelCaptor = ArgumentCaptor.forClass(Object.class);

        verify(modelMock).addAttribute(keyCaptor.capture(), modelCaptor.capture());

        assertThat(keyCaptor.getValue(), is("universes"));
        assertThat(((ArrayList) modelCaptor.getValue()).contains("abc123"), is(true));
        assertThat(((ArrayList) modelCaptor.getValue()).contains("def456"), is(true));
    }

    @Test
    public void universeControllerGetsAUniverseById() {
        when(templateMock.opsForHash()).thenReturn(hashOperationsMock);

        String universeAttributesKey = String.format("universe:" + "abc123");

        when(hashOperationsMock.get(universeAttributesKey, "height")).thenReturn(NON_DEFAULT_HEIGHT);
        when(hashOperationsMock.get(universeAttributesKey, "width")).thenReturn(NON_DEFAULT_WIDTH);

        String response = universeControllerUt.getUniverse(modelMock, "abc123");

        assertThat(response, is("universe"));

        ArgumentCaptor<Object> heightCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Object> widthCaptor = ArgumentCaptor.forClass(Object.class);
        verify(modelMock).addAttribute(eq("height"), heightCaptor.capture());
        verify(modelMock).addAttribute(eq("width"), widthCaptor.capture());

        assertThat(heightCaptor.getValue(), is(NON_DEFAULT_HEIGHT));
        assertThat(widthCaptor.getValue(), is(NON_DEFAULT_WIDTH));
    }
}
