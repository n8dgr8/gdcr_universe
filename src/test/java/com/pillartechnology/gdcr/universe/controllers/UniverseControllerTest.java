package com.pillartechnology.gdcr.universe.controllers;

import com.pillartechnology.gdcr.universe.UniverseApplication;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

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
    public void universeControllerReturnsAListOfKnownUniverseIds() {
        when(universeApplicationMock.getKnownUniverseIds()).thenReturn(fakeUniverseIds);

        ResponseEntity<String> response = universeControllerUt.getUniverses();

        assertThat(response.getStatusCode(), is(OK));

        assertThat(response.getBody().contains("abc123"), is(true));
        assertThat(response.getBody().contains("def456"), is(true));
        assertThat(response.getBody().contains("universes"), is(true));
    }
}
