package com.glaiss.notification.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyFilterTest {

    @InjectMocks
    private ApiKeyFilter apiKeyFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final List<String> VALID_KEYS = List.of("key1", "key2");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(apiKeyFilter, "validApiKeys", VALID_KEYS);
    }

    @Nested
    @DisplayName("Testes para doFilterInternal")
    class DoFilterInternal {

        @Test
        @DisplayName("Dado chave de acesso válida, quando filtrar, então deve continuar a cadeia")
        void dadoChaveValida_quandoFiltrar_entaoDeveContinuarCadeia() throws ServletException, IOException {
            // Dado
            when(request.getHeader("X-CHAVE-DE-ACESSO")).thenReturn("key1");

            // Quando
            apiKeyFilter.doFilterInternal(request, response, filterChain);

            // Então
            verify(filterChain, times(1)).doFilter(request, response);
            verify(response, never()).sendError(anyInt(), anyString());
        }

        @Test
        @DisplayName("Dado chave de acesso inválida, quando filtrar, então deve retornar 401")
        void dadoChaveInvalida_quandoFiltrar_entaoDeveRetornar401() throws ServletException, IOException {
            // Dado
            when(request.getHeader("X-CHAVE-DE-ACESSO")).thenReturn("wrong-key");

            // Quando
            apiKeyFilter.doFilterInternal(request, response, filterChain);

            // Então
            verify(response, times(1)).sendError(eq(401), eq("Chave de acesso inválida"));
            verify(filterChain, never()).doFilter(any(), any());
        }

        @Test
        @DisplayName("Dado chave de acesso ausente, quando filtrar, então deve retornar 401")
        void dadoChaveAusente_quandoFiltrar_entaoDeveRetornar401() throws ServletException, IOException {
            // Dado
            when(request.getHeader("X-CHAVE-DE-ACESSO")).thenReturn(null);

            // Quando
            apiKeyFilter.doFilterInternal(request, response, filterChain);

            // Então
            verify(response, times(1)).sendError(eq(401), eq("Chave de acesso inválida"));
            verify(filterChain, never()).doFilter(any(), any());
        }
    }

    @Nested
    @DisplayName("Testes para shouldNotFilter")
    class ShouldNotFilter {

        @Test
        @DisplayName("Dado caminho público, quando verificar shouldNotFilter, então deve retornar true")
        void dadoCaminhoPublico_quandoVerificar_entaoDeveRetornarTrue() {
            // Dado
            when(request.getRequestURI()).thenReturn("/notification/actuator/health");

            // Quando
            boolean result = apiKeyFilter.shouldNotFilter(request);

            // Então
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Dado caminho privado, quando verificar shouldNotFilter, então deve retornar false")
        void dadoCaminhoPrivado_quandoVerificar_entaoDeveRetornarFalse() {
            // Dado
            when(request.getRequestURI()).thenReturn("/notification/");

            // Quando
            boolean result = apiKeyFilter.shouldNotFilter(request);

            // Então
            assertThat(result).isFalse();
        }
    }
}
