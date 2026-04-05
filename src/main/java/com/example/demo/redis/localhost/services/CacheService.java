package com.example.demo.redis.localhost.services;


import com.example.demo.redis.localhost.lettuce.database.URL;
import com.example.demo.redis.localhost.lettuce.database.URLSerializationCodec;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Log4j2
@Service
public class CacheService {

    //region member variable
    @Value("${service.redis.cache.url}")
    private String url;

    @Value("${service.redis.cache.ttl}")
    private Integer ttl;

    private RedisClient redisClient = null;

    private StatefulRedisConnection<String,URL> statefulRedisConnection = null;
    //endregion

    public URL get(String key) throws ExecutionException, InterruptedException {
        URL url = statefulRedisConnection.async().get(key).get();

        if(url != null){
            log.info("Serving from cache, for key: {}!"+key);
        } else {
            log.info("Cache miss, for the key: {}!"+ key);
        }

        return url;
    }

    public void set(URL url){
        long ttlSeconds = 15L;
        statefulRedisConnection.sync().setex(url.getUrl(),ttlSeconds, url);
    }


    //region construct destruct funstions
    @PostConstruct
    private void init(){
        log.info("Post unit called");
        redisClient = RedisClient.create(url);
        statefulRedisConnection = redisClient.connect(new URLSerializationCodec());
    }

    @PreDestroy
    private void destroy(){
        log.info("Pre destroy called");

        if(statefulRedisConnection != null){
            statefulRedisConnection.close();
        }

        if(redisClient != null){
            redisClient.shutdown();
        }

    }
    //endregion

}
