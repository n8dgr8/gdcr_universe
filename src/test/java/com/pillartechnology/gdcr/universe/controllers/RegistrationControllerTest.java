package com.pillartechnology.gdcr.universe.controllers;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class RegistrationControllerTest {

    RegistrationController registrationControllerUt = new RegistrationController();

    @Test
    public void registrationEndpointReturnsACoordinate() {
        ResponseEntity<String> response = registrationControllerUt.registerCell();

        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), is("A0"));
    }

}
