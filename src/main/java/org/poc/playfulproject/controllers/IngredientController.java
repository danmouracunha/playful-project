package org.poc.playfulproject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.poc.playfulproject.services.IngredientRefreshService;
import org.poc.playfulproject.services.IngredientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private final IngredientRefreshService ingredientRefreshService;

    public IngredientController(IngredientService ingredientService,
                                IngredientRefreshService ingredientRefreshService) {
        this.ingredientService = ingredientService;
        this.ingredientRefreshService = ingredientRefreshService;
    }

    @GetMapping
    public List<String> getIngredients() {
        return ingredientService.getIngredients();
    }

    @GetMapping("/refresh")
    public List<String> refresh() {
        return ingredientService.refreshIngredients();
    }

    @GetMapping("/refresh-async")
    public Map<String, Object> refreshAsync() {
        ingredientRefreshService.triggerBackgroundRefresh();
        return Map.of("status", "refresh-triggered");
    }

    @GetMapping("/evict")
    public Map<String, Object> evict() {
        ingredientService.evictAll();
        return Map.of("status", "evicted");
    }

    @GetMapping("/demo")
    public Map<String, Object> demo() {
        Map<String, Object> result = new HashMap<>();

        long t1 = System.nanoTime();
        List<String> first = ingredientService.getIngredients();
        long firstMs = (System.nanoTime() - t1) / 1_000_000;
        log.info("First call took {} ms", firstMs);

        long t2 = System.nanoTime();
        List<String> second = ingredientService.getIngredients();
        long secondMs = (System.nanoTime() - t2) / 1_000_000;
        log.info("Second call took {} ms", secondMs);

        result.put("firstCallMs", firstMs);
        result.put("secondCallMs", secondMs);
        result.put("firstResult", first);
        result.put("secondResult", second);
        result.put("note", "First call should be slow (external), second should be fast (cache)");
        return result;
    }
}
