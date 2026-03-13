package com.glaiss.notification.config;

import com.glaiss.core.logger.ApiLogginFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import static org.assertj.core.api.Assertions.assertThat;

class FilterConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(FilterConfig.class)
            .withPropertyValues("spring.application.name=notification-test");

    @Test
    @DisplayName("Deve registrar o bean apiLogginFilter corretamente")
    void deveRegistrarApiLogginFilter() {
        contextRunner.run(context -> {
            assertThat(context).hasBean("apiLogginFilter");
            FilterRegistrationBean<?> registrationBean = context.getBean(FilterRegistrationBean.class);
            assertThat(registrationBean.getFilter()).isInstanceOf(ApiLogginFilter.class);
            assertThat(registrationBean.getFilterName()).isEqualTo("ApiLogginFilter");
        });
    }
}
