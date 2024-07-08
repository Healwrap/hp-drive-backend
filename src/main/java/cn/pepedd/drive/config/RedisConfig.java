package cn.pepedd.drive.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis配置类
 *
 * @author pepedd864
 * @since 2023/8/22 18:20
 */
@Configuration
@Slf4j
public class RedisConfig {
  @Bean
  public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    log.info("redisTemplate初始化");
    RedisTemplate redisTemplate = new RedisTemplate();
    // 设置连接工厂
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    // 设置key的序列化器
    redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
    redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());
    // 设置value的序列化器
    redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
    redisTemplate.setHashValueSerializer(redisTemplate.getStringSerializer());
    return redisTemplate;
  }
}
