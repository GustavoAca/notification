package com.glaiss.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectMapperConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(ObjectMapperConfig.class);

    @Test
    @DisplayName("Deve registrar o bean objectMapper corretamente")
    void deveRegistrarObjectMapper() {
        contextRunner.run(context -> {
            assertThat(context).hasBean("objectMapper");
            ObjectMapper mapper = context.getBean(ObjectMapper.class);
            assertThat(mapper).isNotNull();
        });
    }
}
