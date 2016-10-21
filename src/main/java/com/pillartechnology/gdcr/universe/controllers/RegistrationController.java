package com.pillartechnology.gdcr.universe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/universe/@currentUniverse")
public class RegistrationController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<String> registerCell() {
        return new ResponseEntity<>("A0", OK);
    }
}
