package com.zerobase.timate.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer()); // 키는 String 으로 직렬화 설정
		template.setValueSerializer(
			new GenericToStringSerializer<>(Long.class));  // 값은 Long 타입으로 직렬화 설정
		// Long 값을 문자열로 변환하여 Redis 에 저장하고, 가져올 때는 다시 Long 객체로 변환
		return template;
	}

	@Bean
	public RedisTemplate<String, Object> jsonRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());  // Generic JSON 직렬화
		return template;
	}

	@Bean
	// @Cacheable 사용 시 적용되는 설정
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(
			new JavaTimeModule()); // Java 8 날짜/시간 API 지원 (LocalDate, LocalDateTime 등)
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, // 모든 타입을 허용하는 검증기
			ObjectMapper.DefaultTyping.NON_FINAL,
			// 타입 정보를 포함할 클래스의 범위를 지정 -> final 이 아닌 모든 클래스에 대해 타입 정보를 추가
			JsonTypeInfo.As.PROPERTY); // 직렬화된 JSON 에 타입 정보가 별도의 프로퍼티로 추가됨
		/*
		activateDefaultTyping() 하는 이유?
		- activateDefaultTyping()은 ObjectMapper 가 JSON 직렬화/역직렬화 과정에서 객체 타입 정보를 포함하거나 인식하도록 설정하는 메서드이다.
		- 즉, 직렬화된 JSON 데이터에 객체의 타입 정보(클래스 이름)를 추가하여, 역직렬화 시 원래 객체로 정확하게 변환할 수 있게 하는 설정
		- Redis 와 같은 외부 저장소에서 객체를 직렬화해서 저장할 때, 객체의 타입 정보가 없으면 역직렬화 시 원래 타입으로 복원하는게 어렵기 때문에 타입 정보 설정해줘야 함.
		 */

		objectMapper.setVisibility(PropertyAccessor.ALL,
			JsonAutoDetect.Visibility.ANY); // 모든 필드 (public, private, protected) 를 직렬화 대상으로 설정

		// Redis 직렬화 설정
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(
			objectMapper);
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer())) // 값을 JSON 형식으로 직렬화
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(serializer))
			.entryTtl(Duration.ofMinutes(30)); // 전체 캐시의 기본 TTL (30분)

		// RedisCacheManager 빌더로 캐시 설정 구성
		return RedisCacheManager.builder(connectionFactory)
			.cacheDefaults(config)
			.build();
	}


}
