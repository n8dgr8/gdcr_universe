package com.pillartechnology.gdcr.universe.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pillartechnology.gdcr.universe.domain.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class GenerationController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(
            value = "/universe/{universeId}/generation/{generationIndex}",
            method = GET,
            produces = "application/json"
    )
    public ResponseEntity<String> getGeneration(
            Model generationModel,
            @PathVariable String universeId,
            @PathVariable String generationIndex) {

        String universeGuid = universeId;

        if (universeId.equals("@currentUniverse")) {
            universeGuid = redisTemplate.opsForValue().get("current_universe");
        }

        String universeWidth = redisTemplate.opsForHash().get(String.format("universe:%s", universeGuid), "width").toString();
        String universeHeight = redisTemplate.opsForHash().get(String.format("universe:%s", universeGuid), "height").toString();

        String universeGenerationsKey = String.format("universe:%s:generations", universeGuid);
        Object generationGuid = redisTemplate.opsForHash().get(universeGenerationsKey, generationIndex);

        Generation theGeneration;

        if (generationGuid == null) {
            theGeneration = new Generation(Integer.valueOf(universeWidth), Integer.valueOf(universeHeight), universeGuid, redisTemplate);
            System.err.println("This is where we'd create a new one");

            redisTemplate.opsForHash().put(
                    String.format("universe:%s:generations", universeGuid),
                    generationIndex,
                    theGeneration.getGenerationId());

        } else {
            theGeneration = new Generation(generationGuid.toString(), redisTemplate);
        }

        JsonObject generationJsonObject = new JsonObject();

        theGeneration.getWorld().forEach((cellId, cellState) -> {
            generationJsonObject.add(cellId.toString(), new JsonPrimitive(cellState.toString()));
        });

        return new ResponseEntity<>(generationJsonObject.toString(), HttpStatus.OK);
    }

}
