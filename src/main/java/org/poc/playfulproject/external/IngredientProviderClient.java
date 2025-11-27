package org.poc.playfulproject.external;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class IngredientProviderClient {

    public List<String> fetchIngredientsFromExternal() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String timestamp = "fetchedAt=" + Instant.now();
        return List.of("ingredient 1", "ingredient 2", "ingredient 3", timestamp);
    }

    /**
     * Simulates an unstable external provider by randomly throwing an exception.
     * Approximately 50% of the calls will fail.
     * But the first call will always succeed.
     */
    private Boolean randomFailure;
    public List<String> fetchIngredientsFromExternalUnstable() {
        if(randomFailure == null) {
            randomFailure = false;
        } else {
            randomFailure = ThreadLocalRandom.current().nextBoolean();
        }
        if (randomFailure) {
            throw new RuntimeException("Simulated provider failure (random)");
        }
        return fetchIngredientsFromExternal();
    }
}
