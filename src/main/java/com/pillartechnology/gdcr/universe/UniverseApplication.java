package com.pillartechnology.gdcr.universe;

import com.pillartechnology.gdcr.universe.domain.Universe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pillartechnology.gdcr.universe"})
public class UniverseApplication {

    @Autowired
    private RedisTemplate<String, String> template;

	public static void main(String[] args) {
		SpringApplication.run(UniverseApplication.class, args);
	}

    public Map<String, Universe> getKnownUniverses() {
        List<String> knownUniverseGuids = template.opsForList().range("universes", 0, -1);

        Map<String, Universe> knownUniverses = new HashMap<>();

        knownUniverseGuids.forEach(universeGuid -> {
            knownUniverses.put(universeGuid, new Universe(universeGuid));
        });

        return knownUniverses;
    }
}
