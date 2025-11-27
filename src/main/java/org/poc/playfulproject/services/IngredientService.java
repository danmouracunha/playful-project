package org.poc.playfulproject.services;

import org.poc.playfulproject.external.IngredientProviderClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    public static final String INGREDIENTS_CACHE = "ingredients";

    private final IngredientProviderClient providerClient;

    public IngredientService(IngredientProviderClient providerClient) {
        this.providerClient = providerClient;
    }

    @Cacheable(value = INGREDIENTS_CACHE)
    public List<String> getIngredients() {
        return providerClient.fetchIngredientsFromExternal();
    }

    @CachePut(value = INGREDIENTS_CACHE)
    public List<String> refreshIngredients() {
        return providerClient.fetchIngredientsFromExternal();
    }

    @CacheEvict(value = INGREDIENTS_CACHE, allEntries = true)
    public void evictAll() {
        // no-op, annotation does the work
    }
}
