package org.greenatom.forum.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(@NotNull @Bean Page page) {
    public record Page(@NotNull Long number, @NotNull Long size) {

    }

}
