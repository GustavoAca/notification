package com.glaiss.notification.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    @DisplayName("Dado dados válidos, quando criar e-mail via builder, então deve preencher campos corretamente")
    void dadoDadosValidos_quandoCriarViaBuilder_entaoDevePreencherCampos() {
        // Dado
        UUID id = UUID.randomUUID();
        String para = "teste@example.com";
        String assunto = "Assunto";
        String corpo = "Corpo";
        String codigo = "123456";
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(15);

        // Quando
        Email email = Email.builder()
                .id(id)
                .para(para)
                .assunto(assunto)
                .corpo(corpo)
                .codigoDeAutenticacao(codigo)
                .dataExpiracao(expiracao)
                .build();

        // Então
        assertThat(email.getId()).isEqualTo(id);
        assertThat(email.getPara()).isEqualTo(para);
        assertThat(email.getAssunto()).isEqualTo(assunto);
        assertThat(email.getCorpo()).isEqualTo(corpo);
        assertThat(email.getCodigoDeAutenticacao()).isEqualTo(codigo);
        assertThat(email.getDataExpiracao()).isEqualTo(expiracao);
    }

    @Test
    @DisplayName("Testar setters e getters")
    void testSettersAndGetters() {
        Email email = Email.builder().build();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        email.setId(id);
        email.setPara("para");
        email.setAssunto("assunto");
        email.setCorpo("corpo");
        email.setCodigoDeAutenticacao("123");
        email.setDataExpiracao(now);

        assertThat(email.getId()).isEqualTo(id);
        assertThat(email.getPara()).isEqualTo("para");
        assertThat(email.getAssunto()).isEqualTo("assunto");
        assertThat(email.getCorpo()).isEqualTo("corpo");
        assertThat(email.getCodigoDeAutenticacao()).isEqualTo("123");
        assertThat(email.getDataExpiracao()).isEqualTo(now);
    }
}
