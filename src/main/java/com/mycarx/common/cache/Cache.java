package com.mycarx.common.cache;

import java.util.concurrent.TimeUnit;

public interface Cache {

    String getName();

    Object getNativeCache();

    Object get(Object key);

    <T> T get(Object key, Class<T> type);

    void set(Object key, Object value);

    void set(Object key, Object value, long expTime);

    void set(Object key, Object value, long expTime, TimeUnit timeUnit);

    void del(Object key);

    void clear();
}
