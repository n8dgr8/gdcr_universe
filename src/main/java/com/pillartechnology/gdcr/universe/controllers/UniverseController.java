package com.pillartechnology.gdcr.universe.controllers;

import com.pillartechnology.gdcr.universe.UniverseApplication;
import com.pillartechnology.gdcr.universe.domain.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/universe")
public class UniverseController {

    private UniverseApplication universeApplication;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UniverseController(UniverseApplication newUniverse) {
        universeApplication = newUniverse;
    }

    @RequestMapping(value = "/{universeId}", method = GET)
    public ResponseEntity<String> getUniverseById(@PathVariable String universeId) {
        ResponseEntity<String> theResponse = new ResponseEntity<>(NOT_FOUND);

        Universe newUniverse = new Universe();
        universeApplication.getKnownUniverses().put(newUniverse.getUniverseId(), newUniverse);

        if (universeApplication.getKnownUniverses().containsKey(universeId)) {
            Universe theUniverse = universeApplication.getKnownUniverses().get(universeId);
            theResponse = new ResponseEntity<>(String.format("UNIVERSE %s", theUniverse.getUniverseId()), OK);
        }

        System.err.println("Does it or doesn't it? " + redisTemplate.hasKey(universeId));

        return theResponse;
    }

}
