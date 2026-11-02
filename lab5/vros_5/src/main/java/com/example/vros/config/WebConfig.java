package com.example.vros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class WebConfig {

    // ETAG: filtru care calculeaza un ETag (hash MD5) din corpul raspunsului
    // si raspunde 304 Not Modified daca clientul trimite acelasi ETag in If-None-Match
    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
