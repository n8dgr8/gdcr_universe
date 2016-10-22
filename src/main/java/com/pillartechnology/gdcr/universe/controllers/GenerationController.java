package com.pillartechnology.gdcr.universe.controllers;

import com.pillartechnology.gdcr.universe.domain.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GenerationController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(
            value = "/universe/{universeId}/generation/{generationIndex}",
            method = GET
    )
    public String getGeneration(
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

            redisTemplate.opsForHash().put(
                    String.format("universe:%s:generations", universeGuid),
                    generationIndex,
                    theGeneration.getGenerationId());

        } else {
            theGeneration = new Generation(generationGuid.toString(), redisTemplate);
        }

        List<String> rows = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        IntStream.range(0, Integer.valueOf(universeHeight)).forEach(row -> {
            rows.add(Character.toString(Generation.ALPHABET[row]));
        });


        IntStream.range(0, Integer.valueOf(universeWidth)).forEach(column -> {
            columns.add(String.valueOf(column));
        });

        generationModel.addAttribute("rows", rows);
        generationModel.addAttribute("columns", columns);

        Integer prevIndex = Integer.valueOf(generationIndex) - 1;
        Integer nextIndex = Integer.valueOf(generationIndex) + 1;

        generationModel.addAttribute("generation_id", generationIndex);
        generationModel.addAttribute("prev_url", prevIndex);
        generationModel.addAttribute("next_url", nextIndex);

        generationModel.addAttribute("cells", theGeneration.getWorld());

        return "generation";
    }

}
