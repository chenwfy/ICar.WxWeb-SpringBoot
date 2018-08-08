package com.mycarx.common.cache;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.transcoders.SerializingTranscoder;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class MemcachedCache implements Cache {
    private MemcachedClient memcachedClient;
    public static final int DEFAULT_TIME_EXP = 86400;

    public MemcachedCache(String hosts) {
        this(hosts, null, null);
    }

    public MemcachedCache(String hosts, String username, String password) {
        String[] memcacheHost = hosts.split(",");
        List<InetSocketAddress> socketAddresses = new ArrayList<>(memcacheHost.length);

        for (int memcachedClient = 0; memcachedClient < memcacheHost.length; ++memcachedClient) {
            String authDescriptor = memcacheHost[memcachedClient];
            String[] arr = authDescriptor.split(":");
            socketAddresses.add(new InetSocketAddress(arr[0], Integer.parseInt(arr[1])));
        }

        try {
            if (StringUtils.isNotBlank(username)) {
                AuthDescriptor authDescriptor = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(username, password));
                memcachedClient = new MemcachedClient((new ConnectionFactoryBuilder()).setTranscoder(new CustomSerializingTranscoder()).setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setAuthDescriptor(authDescriptor).build(), socketAddresses);
            } else {
                memcachedClient = new MemcachedClient((new ConnectionFactoryBuilder()).setTranscoder(new CustomSerializingTranscoder()).setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).build(), socketAddresses);
            }

        } catch (IOException e) {
            throw new RuntimeException("初始化memcache失败，" + e.getMessage());
        }

    }

    private MemcachedClient getCache() {
        return memcachedClient;
    }

    @Override
    public String getName() {
        return getCache().getClass().getName();
    }

    @Override
    public Object getNativeCache() {
        return getCache();
    }

    @Override
    public Object get(Object key) {
        MemcachedClient memcachedClient = getCache();
        if (memcachedClient != null) {
            try {
                return memcachedClient.get((String) key);
            } catch (OperationTimeoutException var3) {
                return null;
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return (T) get(key);
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
        if (value == null || key == null) return;
        long time = expTime;
        switch (timeUnit) {
            case DAYS:
                time *= 24 * 60 * 60;
                break;
            case HOURS:
                time *= 60 * 60;
                break;
            case MINUTES:
                time *= 60;
                break;
            case MILLISECONDS:
                time /= 1000;
                break;
        }
        memcachedClient.add((String) key, (int) time, value);
    }

    @Override
    public void del(Object key) {
        getCache().delete((String) key);
    }

    @Override
    public void clear() {
        memcachedClient.flush();
    }

    private class CustomSerializingTranscoder extends SerializingTranscoder {
        @Override
        protected Object deserialize(byte[] bytes) {
            final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            ObjectInputStream in = null;
            try {
                ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
                in = new ObjectInputStream(bs) {
                    @Override
                    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                        try {
                            return currentClassLoader.loadClass(objectStreamClass.getName());
                        } catch (Exception e) {
                            return super.resolveClass(objectStreamClass);
                        }
                    }
                };
                return in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                closeStream(in);
            }
        }

        private void closeStream(ObjectInputStream in) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
