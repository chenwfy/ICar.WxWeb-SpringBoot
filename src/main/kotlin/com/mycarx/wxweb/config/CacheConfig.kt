package com.mycarx.wxweb.config

import com.mycarx.common.cache.CacheClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class CacheConfig {
    @Bean
    fun cacheClient(env: Environment): CacheClient {
        return CacheClient(env)
    }
}