package com.zerobase.timate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer()); // 키는 String 으로 직렬화 설정
		template.setValueSerializer(new GenericToStringSerializer<>(Long.class));  // 값은 Long 타입으로 직렬화 설정
            // Long 값을 문자열로 변환하여 Redis 에 저장하고, 가져올 때는 다시 Long 객체로 변환
		return template;
	}

}
