package com.mycarx.common.cache;

import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class CacheClient {
    public static Cache instance;
    private static final String MEMCACHE = "memcache";
    private static final String REDIS = "redis";
    private static String type;

    public CacheClient(Environment environment) {
        type = environment.getProperty("cache", MEMCACHE);
        if (MEMCACHE.equals(type)) {
            CacheClient.instance = new MemcachedCache(environment.getProperty("memcache.host"));
        } else {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(environment.getProperty("redis.pool.max-idle", Integer.class));
            poolConfig.setMinIdle(environment.getProperty("redis.pool.min-idle", Integer.class));
            poolConfig.setMaxWaitMillis(environment.getProperty("redis.pool.max-wait", Long.class));
            JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
            connectionFactory.setHostName(environment.getProperty("redis.host"));
            connectionFactory.setPort(environment.getProperty("redis.port", Integer.class, 6379));
            connectionFactory.setDatabase(environment.getProperty("redis.database", Integer.class, 0));
            connectionFactory.setPassword(environment.getProperty("redis.password"));
            connectionFactory.setTimeout(environment.getProperty("redis.timeout", Integer.class, 30));
            JedisShardInfo jedisShardInfo = new JedisShardInfo(connectionFactory.getHostName(), connectionFactory.getPort(), connectionFactory.getTimeout(), "cache");
            jedisShardInfo.setPassword(connectionFactory.getPassword());
            connectionFactory.setShardInfo(jedisShardInfo);
            CacheClient.instance = new RedisCache(connectionFactory);
        }
    }
}
