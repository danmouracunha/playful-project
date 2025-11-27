package org.poc.playfulproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PlayfulProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayfulProjectApplication.class, args);
    }

}
