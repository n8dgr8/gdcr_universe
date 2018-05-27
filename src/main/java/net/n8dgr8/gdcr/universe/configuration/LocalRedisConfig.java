package net.n8dgr8.gdcr.universe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class LocalRedisConfig {
    @Bean
    public RedisConnectionFactory jedisConnectionFactory() throws URISyntaxException {
        String redisUrl = System.getenv("REDIS_URL");

        if (redisUrl == null) {
            redisUrl = "redis://localhost:6379";
        }

        URI redisUri = new URI(redisUrl);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);

        jedisConnectionFactory.setHostName(redisUri.getHost());
        jedisConnectionFactory.setPort(redisUri.getPort());
        jedisConnectionFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);

        if (redisUri.getUserInfo() != null) {
            jedisConnectionFactory.setPassword(redisUri.getUserInfo().split(":", 2)[1]);
        }

        return jedisConnectionFactory;
    }
}
