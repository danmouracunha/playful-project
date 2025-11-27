package org.poc.playfulproject.services;

import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import static org.springframework.cache.interceptor.SimpleKey.EMPTY;

@Service
public class IngredientRefreshService {

    private final CacheManager cacheManager;

    public IngredientRefreshService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Triggers a background refresh of the ingredients cache. If the refresh fails,
     * Caffeine will retain and continue to serve the previous value.
     */
    public void triggerBackgroundRefresh() {
        Cache cache = cacheManager.getCache(IngredientService.INGREDIENTS_CACHE);
        if (cache instanceof CaffeineCache cc) {
            LoadingCache<Object, Object> nativeCache = (LoadingCache<Object, Object>) cc.getNativeCache();
            nativeCache.refresh(EMPTY);
        }
    }
}
