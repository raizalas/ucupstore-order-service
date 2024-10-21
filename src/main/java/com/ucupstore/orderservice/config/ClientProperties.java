package com.ucupstore.orderservice.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "ucup")
public record ClientProperties(
        @NotNull
        URI catalogServiceUri
) { }
