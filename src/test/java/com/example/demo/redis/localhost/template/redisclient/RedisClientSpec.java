package com.example.demo.redis.localhost.template.redisclient;

import com.example.demo.redis.localhost.lettuce.database.URL;
import com.example.demo.redis.localhost.services.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

/**
 * this not working
 * Test to experiment with redisclient
 */
@SpringBootTest
public class RedisClientSpec {

    private final String strinKey = "https://example.com/set_test";

    @Autowired
    private CacheService cachesv;

    @Test
    void store_and_get_key() throws ExecutionException, InterruptedException {

        URL testUrl = new URL();

        testUrl.setUrl(strinKey);
        cachesv.set(testUrl);

        var result =  cachesv.get(strinKey);

        //Assertions.assertEquals(result);
    }
}
