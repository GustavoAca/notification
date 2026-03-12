package com.glaiss.notification.domain.service;

import com.glaiss.notification.controller.dto.EmailDetails;
import com.glaiss.notification.controller.dto.EmailValidador;
import com.glaiss.notification.domain.model.Email;
import com.glaiss.notification.domain.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private EmailRepository repo;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private final String SENDER = "sender@example.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "sender", SENDER);
    }

    @Nested
    @DisplayName("Testes para o método sendMail")
    class SendMail {

        private EmailDetails emailDetails;

        @BeforeEach
        void setup() {
            emailDetails = new EmailDetails("destinatario@example.com", "Assunto", "Corpo");
        }

        @Test
        @DisplayName("Dado detalhes válidos, quando enviar e-mail, então deve enviar e salvar com sucesso")
        void dadoDetalhesValidos_quandoEnviarEmail_entaoDeveEnviarESalvarComSucesso() throws Exception {
            // Dado
            when(repo.findByPara(anyString())).thenReturn(Optional.empty());
            when(repo.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Quando
            emailService.sendMail(emailDetails);

            // Então
            verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
            verify(repo, times(1)).save(any(Email.class));
        }

        @Test
        @DisplayName("Dado erro no envio, quando enviar e-mail, então deve propagar a exceção e não salvar")
        void dadoErroNoEnvio_quandoEnviarEmail_entaoDevePropagarExcecaoENaoSalvar() {
            // Dado
            doThrow(new RuntimeException("Erro no envio")).when(javaMailSender).send(any(SimpleMailMessage.class));

            // Quando & Então
            assertThatThrownBy(() -> emailService.sendMail(emailDetails))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no envio");

            verify(repo, never()).save(any(Email.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método salvar")
    class Salvar {

        @Test
        @DisplayName("Dado um e-mail para usuário existente, quando salvar, então deve deletar o anterior e salvar o novo")
        void dadoUsuarioExistente_quandoSalvar_entaoDeveDeletarAnteriorESalvarNovo() {
            // Dado
            String para = "usuario@example.com";
            Email emailNovo = Email.builder().para(para).build();
            Email emailExistente = Email.builder().id(UUID.randomUUID()).para(para).build();

            when(repo.findByPara(para)).thenReturn(Optional.of(emailExistente));
            when(repo.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Quando
            emailService.salvar(emailNovo);

            // Então
            verify(repo, times(1)).deleteById(emailExistente.getId());
            verify(repo, times(1)).save(emailNovo);
        }

        @Test
        @DisplayName("Dado um e-mail para usuário novo, quando salvar, então deve apenas salvar o novo")
        void dadoUsuarioNovo_quandoSalvar_entaoDeveApenasSalvarNovo() {
            // Dado
            String para = "novo@example.com";
            Email emailNovo = Email.builder().para(para).build();

            when(repo.findByPara(para)).thenReturn(Optional.empty());
            when(repo.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Quando
            emailService.salvar(emailNovo);

            // Então
            verify(repo, never()).deleteById(any());
            verify(repo, times(1)).save(emailNovo);
        }
    }

    @Nested
    @DisplayName("Testes para o método validarCodigoAutenticacao")
    class ValidarCodigoAutenticacao {

        private final String USUARIO = "usuario@example.com";
        private final String CODIGO = "123456";

        @Test
        @DisplayName("Dado código correto e dentro do prazo, quando validar, então deve retornar true e deletar registro")
        void dadoCodigoCorretoEDentroDoPrazo_quandoValidar_entaoDeveRetornarTrueEDeletar() {
            // Dado
            EmailValidador validador = new EmailValidador(CODIGO, USUARIO);
            Email email = Email.builder()
                    .id(UUID.randomUUID())
                    .para(USUARIO)
                    .codigoDeAutenticacao(CODIGO)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(5))
                    .build();

            when(repo.findByPara(USUARIO)).thenReturn(Optional.of(email));

            // Quando
            Boolean resultado = emailService.validarCodigoAutenticacao(validador);

            // Então
            assertThat(resultado).isTrue();
            verify(repo, times(1)).deleteById(email.getId());
        }

        @Test
        @DisplayName("Dado código incorreto, quando validar, então deve retornar false")
        void dadoCodigoIncorreto_quandoValidar_entaoDeveRetornarFalse() {
            // Dado
            EmailValidador validador = new EmailValidador("WRONG", USUARIO);
            Email email = Email.builder()
                    .para(USUARIO)
                    .codigoDeAutenticacao(CODIGO)
                    .dataExpiracao(LocalDateTime.now().plusMinutes(5))
                    .build();

            when(repo.findByPara(USUARIO)).thenReturn(Optional.of(email));

            // Quando
            Boolean resultado = emailService.validarCodigoAutenticacao(validador);

            // Então
            assertThat(resultado).isFalse();
            verify(repo, never()).deleteById(any());
        }

        @Test
        @DisplayName("Dado código expirado, quando validar, então deve retornar false")
        void dadoCodigoExpirado_quandoValidar_entaoDeveRetornarFalse() {
            // Dado
            EmailValidador validador = new EmailValidador(CODIGO, USUARIO);
            Email email = Email.builder()
                    .para(USUARIO)
                    .codigoDeAutenticacao(CODIGO)
                    .dataExpiracao(LocalDateTime.now().minusMinutes(1))
                    .build();

            when(repo.findByPara(USUARIO)).thenReturn(Optional.of(email));

            // Quando
            Boolean resultado = emailService.validarCodigoAutenticacao(validador);

            // Então
            assertThat(resultado).isFalse();
            verify(repo, never()).deleteById(any());
        }

        @Test
        @DisplayName("Dado usuário não encontrado, quando validar, então deve retornar false")
        void dadoUsuarioNaoEncontrado_quandoValidar_entaoDeveRetornarFalse() {
            // Dado
            EmailValidador validador = new EmailValidador(CODIGO, USUARIO);
            when(repo.findByPara(USUARIO)).thenReturn(Optional.empty());

            // Quando
            Boolean resultado = emailService.validarCodigoAutenticacao(validador);

            // Então
            assertThat(resultado).isFalse();
            verify(repo, never()).deleteById(any());
        }
    }
}
