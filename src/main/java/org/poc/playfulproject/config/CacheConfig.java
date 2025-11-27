package org.poc.playfulproject.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.poc.playfulproject.external.IngredientProviderClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineSpec() {
        return Caffeine.newBuilder()
                .maximumSize(1_000)
                .refreshAfterWrite(60, TimeUnit.SECONDS)
                .expireAfterWrite(6, TimeUnit.HOURS);
    }

    @Bean
    public CacheLoader<Object, Object> caffeineCacheLoader(IngredientProviderClient provider) {
        return new CacheLoader<>() {
            @Override
            public Object load(Object key) {
                return provider.fetchIngredientsFromExternalUnstable();
            }

            @Override
            public Object reload(Object key, Object oldValue) {
                // Use unstable provider to simulate random failures during refresh
                // If this throws, Caffeine retains oldValue and continues serving it
                return provider.fetchIngredientsFromExternalUnstable();
            }
        };
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine,
                                     CacheLoader<Object, Object> loader) {
        CaffeineCacheManager manager = new CaffeineCacheManager("ingredients");
        // Important: set the CacheLoader BEFORE applying the Caffeine builder,
        // otherwise caches may be created as non-loading and refreshAfterWrite will fail.
        manager.setCacheLoader(loader); // Enables LoadingCache + refresh semantics
        manager.setCaffeine(caffeine);
        return manager;
    }
}
