package com.example.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 17:52
 * @desc
 */
@Configuration
public class JedisConfig {

  @Value("${ads.redis.host}")
  private String host;
  @Value("${ads.redis.port:6379}")
  private int port;
  @Value("${ads.redis.database:0}")
  private int database;
  @Value("${ads.redis.password}")
  private String password;
  @Value("${ads.redis.timeout:3000}")
  private int timeout;
  @Bean
  JedisPoolConfig jedisPoolConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(100);
    jedisPoolConfig.setTestOnBorrow(true);
    jedisPoolConfig.setTestOnReturn(true);
    return jedisPoolConfig;
  }
  @Bean
  JedisPool jedisPool() {
    JedisPool jedisPool = new JedisPool(jedisPoolConfig(), host, port, timeout,
        StringUtils.trimToNull(password), database);
    return jedisPool;
  }
}
