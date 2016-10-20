package com.pillartechnology.gdcr.universe.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pillartechnology.gdcr.universe.UniverseApplication;
import com.pillartechnology.gdcr.universe.domain.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
        universe.setUniverseHeight(height);
        universe.setUniverseWidth(width);

        return new ResponseEntity<>(universe.getUniverseId(), CREATED);
    }

    @RequestMapping("/{universeId}")
    public String getUniverse(Model universeModel, @PathVariable String universeId) {

        String key = String.format("universe:" + universeId);

        String universeHeight = redisTemplate.opsForHash().get(key, "height").toString();
        String universeWidth = redisTemplate.opsForHash().get(key, "width").toString();

        universeModel.addAttribute("universe_id", universeId);
        universeModel.addAttribute("height", universeHeight);
        universeModel.addAttribute("width", universeWidth);

        return "universe";
    }
}
