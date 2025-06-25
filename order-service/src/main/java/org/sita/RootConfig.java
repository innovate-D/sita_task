package org.sita;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class RootConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

    @Bean
    public CacheManager cacheManager() {
        var manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES));
        return manager;
    }
}