package com.example.demo.redis.localhost.template;

import com.example.demo.redis.localhost.RedisTemplateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * sample how save and get with RedisTemplate
 */
@SpringBootTest
public class TemplateSpec {

    private final String keyName = "demoredis:testkey:10:test";

    @Autowired
    private RedisTemplateService tedisTemplateSv;

    @Test
    void simple_smoke_test(){

        String jsonString = "{\"name\":\"leto\",\"planet\":\"dunde\",\"likes\":[\"spice\"]}";

        tedisTemplateSv.save(keyName,jsonString);

        var result = tedisTemplateSv.get(keyName);

        Assertions.assertEquals(result,jsonString);
    }

}
