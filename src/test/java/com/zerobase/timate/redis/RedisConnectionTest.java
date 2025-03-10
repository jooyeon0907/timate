package com.zerobase.timate.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String key = "testKey";
    private String value = "Hello, Redis!";

    // 테스트 전에 Redis 에 값을 설정
    @BeforeEach
    public void setUp() {
        redisTemplate.opsForValue().set(key, value);
    }

    @Test
    public void testRedisConnection() {
        String result = redisTemplate.opsForValue().get(key);
        assertEquals(value, result);
    }

    @Test
    public void testDeleteFromRedis() {
        redisTemplate.delete(key);

        String result = redisTemplate.opsForValue().get(key);
        assertNull(result);
    }



}