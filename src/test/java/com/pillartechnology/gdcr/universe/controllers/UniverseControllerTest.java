package com.pillartechnology.gdcr.universe.controllers;

import com.pillartechnology.gdcr.universe.UniverseApplication;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UniverseControllerTest {

    private UniverseApplication universeApplicationMock = mock(UniverseApplication.class);

    private UniverseController universeControllerUt = new UniverseController(universeApplicationMock);

    private List<String> fakeUniverseIds = new ArrayList<>();

    @Before
    public void setUp() {
        fakeUniverseIds.add("abc123");
        fakeUniverseIds.add("def456");
    }

    @Test
    public void universeControllerSetsModelWithKnownUniverses() {
        when(universeApplicationMock.getKnownUniverseIds()).thenReturn(fakeUniverseIds);

        Model modelMock = mock(Model.class);

        String response = universeControllerUt.getUniverses(modelMock);

        assertThat(response, is("universes"));

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> modelCaptor = ArgumentCaptor.forClass(Object.class);

        verify(modelMock).addAttribute(keyCaptor.capture(), modelCaptor.capture());

        assertThat(keyCaptor.getValue(), is("universes"));
        assertThat(((ArrayList) modelCaptor.getValue()).contains("abc123"), is(true));
        assertThat(((ArrayList) modelCaptor.getValue()).contains("def456"), is(true));
    }
}
