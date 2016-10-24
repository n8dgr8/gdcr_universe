package com.pillartechnology.gdcr.universe.controllers;

import com.google.gson.JsonObject;
import com.pillartechnology.gdcr.universe.domain.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class CellStateController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(
            value = "/universe/{universeId}/generation/{generationIndex}/cell/{cellId}",
            method = GET,
            produces = "application/json")
    public ResponseEntity<String> getCellState(@PathVariable String universeId,
                                               @PathVariable String generationIndex,
                                               @PathVariable String cellId
    ) {
        Generation theGeneration = getGenerationByUniverseIdAndGenerationIndex(universeId, generationIndex);

        Generation.E_CellState cellState = Generation.E_CellState.UNKNOWN;

        if (theGeneration.getWorld().containsKey(cellId)) {
            cellState = theGeneration.getWorld().get(cellId);
        }

        JsonObject generationJsonObject = new JsonObject();
        generationJsonObject.addProperty("cellState", cellState.toString());

        theGeneration.getWorld().get(cellId);

        return new ResponseEntity<>(generationJsonObject.toString(), OK);
    }

    @RequestMapping(
            value = "/universe/{universeId}/generation/{generationIndex}/cell/{cellId}",
            method = POST,
            produces = "application/json")
    public ResponseEntity<String> setCellState(@PathVariable String universeId,
                                               @PathVariable String generationIndex,
                                               @PathVariable String cellId,
                                               @RequestBody String requestBody) {

        Generation theGeneration = getGenerationByUniverseIdAndGenerationIndex(universeId, generationIndex);

        Generation.E_CellState cellState = Generation.E_CellState.valueOf(requestBody);

        theGeneration.setCellState(cellId, cellState);

        return new ResponseEntity<>("{}", CREATED);

    }

    private Generation getGenerationByUniverseIdAndGenerationIndex(@PathVariable String universeId, @PathVariable String generationIndex) {
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

            redisTemplate.opsForHash().put(
                    String.format("universe:%s:generations", universeGuid),
                    generationIndex,
                    theGeneration.getGenerationId());
        } else {
            theGeneration = new Generation(generationGuid.toString(), redisTemplate);
        }

        return theGeneration;
    }

}
