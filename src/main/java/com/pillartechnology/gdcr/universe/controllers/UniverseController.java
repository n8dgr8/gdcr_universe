package com.pillartechnology.gdcr.universe.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pillartechnology.gdcr.universe.UniverseApplication;
import com.pillartechnology.gdcr.universe.domain.Generation;
import com.pillartechnology.gdcr.universe.domain.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.ALIVE;
import static com.pillartechnology.gdcr.universe.domain.Generation.E_CellState.DEAD;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/universe")
public class UniverseController {

    private UniverseApplication universeApplication;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UniverseController(UniverseApplication newUniverse) {
        universeApplication = newUniverse;
    }

    @RequestMapping(method = GET)
    public String getUniverses(Model universesModel) {
        universesModel.addAttribute("universes", universeApplication.getKnownUniverseIds());
        return "universes";
    }

    @RequestMapping(method = POST)
    public ResponseEntity<String> createUniverse(@RequestBody String postContents) {
        Gson gson = new Gson();

        JsonObject postContentsObject = gson.fromJson(postContents, JsonObject.class);

        Integer width = postContentsObject.get("width").getAsInt();
        Integer height = postContentsObject.get("height").getAsInt();

        Universe universe = new Universe(redisTemplate);
        redisTemplate.opsForList().rightPush("universes", universe.getUniverseId());

        universe.setUniverseHeight(height);
        universe.setUniverseWidth(width);

        Random rando = new Random();

        Generation firstGeneration = new Generation(width, height, universe.getUniverseId(), redisTemplate);
        firstGeneration.getWorld().forEach((cellId, cellState) -> {

            if (rando.nextBoolean()) {
                firstGeneration.setCellState(cellId, ALIVE);
            } else {
                firstGeneration.setCellState(cellId, DEAD);
            }

            redisTemplate.opsForList().rightPush(String.format("universe:%s:registration", universe.getUniverseId()), cellId);
        });

        redisTemplate.opsForHash().put(
                String.format("universe:%s:generations", universe.getUniverseId()),
                "0",
                firstGeneration.getGenerationId());

        redisTemplate.opsForValue().set("current_universe", universe.getUniverseId());

        return new ResponseEntity<>(universe.getUniverseId(), CREATED);
    }

    @RequestMapping(value = "/{universeId}", method = GET)
    public String getUniverse(Model universeModel, @PathVariable String inUniverseId) {

        String universeId = inUniverseId;

        if (universeId.equals("@currentUniverse")) {
            universeId = redisTemplate.opsForValue().get("current_universe");
        }

        String key = String.format("universe:" + universeId);

        String universeHeight = redisTemplate.opsForHash().get(key, "height").toString();
        String universeWidth = redisTemplate.opsForHash().get(key, "width").toString();

        universeModel.addAttribute("universe_id", universeId);
        universeModel.addAttribute("height", universeHeight);
        universeModel.addAttribute("width", universeWidth);

        return "universe";
    }
}
