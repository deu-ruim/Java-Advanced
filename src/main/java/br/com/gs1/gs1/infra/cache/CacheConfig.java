package br.com.gs1.gs1.infra.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "modelos",
                "modelo",
                "desastre",
                "desastres"
        );
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)  // Cache expires 30 minutes after write
                .maximumSize(1000)                    // Maximum 1000 entries in cache
                .recordStats());                      // Enable cache statistics
        return cacheManager;
    }
}