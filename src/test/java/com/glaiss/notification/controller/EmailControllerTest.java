package com.glaiss.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glaiss.core.jpa.test.GlaissWebMvcTest;
import com.glaiss.notification.controller.dto.EmailDetails;
import com.glaiss.notification.controller.dto.EmailValidador;
import com.glaiss.notification.domain.service.EmailService;
import com.glaiss.notification.domain.repository.EmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@GlaissWebMvcTest(controllers = EmailController.class, properties = {
        "app.api-keys=teste-123",
        "spring.application.name=notification"
})
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private EmailRepository emailRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Testes para o endpoint POST /notificacao-por-email")
    class SendMail {

        @Test
        @DisplayName("Dado detalhes de e-mail válidos, quando chamar sendMail, então deve retornar 200")
        void dadoDetalhesValidos_quandoChamarSendMail_entaoDeveRetornar200() throws Exception {
            // Dado
            EmailDetails details = new EmailDetails("teste@example.com", "Assunto", "Corpo");
            doNothing().when(emailService).sendMail(any(EmailDetails.class));

            // Quando & Então
            mockMvc.perform(post("/notificacao-por-email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(details))
                            .header("X-CHAVE-DE-ACESSO", "teste-123"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Testes para o endpoint POST /notificacao-por-email/validar-codigo")
    class ValidarCodigoAutenticacao {

        @Test
        @DisplayName("Dado validador válido, quando chamar validarCodigoAutenticacao, então deve retornar resultado")
        void dadoValidadorValido_quandoChamarValidar_entaoDeveRetornarResultado() throws Exception {
            // Dado
            EmailValidador validador = new EmailValidador("123456", "usuario@example.com");
            when(emailService.validarCodigoAutenticacao(any(EmailValidador.class))).thenReturn(true);

            // Quando & Então
            mockMvc.perform(post("/notificacao-por-email/validar-codigo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validador))
                            .header("X-CHAVE-DE-ACESSO", "teste-123"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }
    }
}
