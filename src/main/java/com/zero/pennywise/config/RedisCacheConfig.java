//package com.zero.pennywise.config;
//
//
//import com.zero.pennywise.service.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@RequiredArgsConstructor
//public class RedisCacheConfig {
//
//  @Value("${spring.data.redis.host}")
//  private String host;
//
//  @Value("${spring.data.redis.port}")
//  private int port;
//
//  private final NotificationService notificationService;
//
//  @Bean
//  public RedisConnectionFactory redisConnectionFactory() {
//    return new LettuceConnectionFactory(host, port);
//  }
//
//  @Bean
//  public RedisTemplate<String, Object> redisTemplate() {
//    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(redisConnectionFactory());
//    redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
//    return redisTemplate;
//  }
//
//  @Bean
//  public CacheManager cacheManager() {
//    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//        .serializeKeysWith(RedisSerializationContext.SerializationPair
//            .fromSerializer(new StringRedisSerializer()))
//        .serializeValuesWith(RedisSerializationContext.SerializationPair
//            .fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//    return RedisCacheManager.builder(redisConnectionFactory())
//        .cacheDefaults(redisCacheConfiguration)
//        .build();
//  }
//
//  @Bean
//  public MessageListenerAdapter messageListener() {
//    return new MessageListenerAdapter(notificationService);
//  }
//
//
//  //컨테이너 설정
//  @Bean
//  RedisMessageListenerContainer redisContainer() {
//    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//    container.setConnectionFactory(redisConnectionFactory());
//    container.addMessageListener(messageListener(), new ChannelTopic("notifications"));
//    return container;
//  }
//
//}
