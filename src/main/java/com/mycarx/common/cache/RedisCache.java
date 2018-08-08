package com.mycarx.common.cache;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class RedisCache implements Cache {
    private ObjectRedisTemplate redisClient;

    public static final int DEFAULT_TIME_EXP = 86400;

    public RedisCache(RedisConnectionFactory redisConnectionFactory) {
        redisClient = new ObjectRedisTemplate(redisConnectionFactory);
    }

    @Override
    public String getName() {
        return redisClient.getConnectionFactory().getConnection().getClientName();
    }

    @Override
    public Object getNativeCache() {
        return redisClient.getConnectionFactory().getConnection().getNativeConnection();
    }

    @Override
    public Object get(Object key) {
        return redisClient.opsForValue().get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return (T) redisClient.opsForValue().get(key);
    }

    @Override
    public void set(Object key, Object value) {
        set(key, value, DEFAULT_TIME_EXP);
    }

    @Override
    public void set(Object key, Object value, long expTime) {
        set(key, value, expTime, TimeUnit.SECONDS);
    }

    @Override
    public void set(Object key, Object value, long expTime, TimeUnit timeUnit) {
        if (key == null || value == null) {
            return;
        }
        redisClient.opsForValue().set(key, value, expTime, timeUnit);
    }

    @Override
    public void del(Object key) {
        redisClient.delete(key);
    }

    @Override
    public void clear() {
        redisClient.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }
}
