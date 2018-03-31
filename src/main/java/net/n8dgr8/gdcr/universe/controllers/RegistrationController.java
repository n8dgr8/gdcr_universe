package net.n8dgr8.gdcr.universe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/universe/@currentUniverse")
public class RegistrationController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "/register", method = GET)
    public ResponseEntity<String> registerCell() {
        String currentUniverseId = redisTemplate.opsForValue().get("current_universe");

        String cellId = redisTemplate.opsForList().leftPop(String.format("universe:%s:registration", currentUniverseId));

        return new ResponseEntity<>(cellId, OK);
    }
}
