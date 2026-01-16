package com.glaiss.notification.domain.service;

import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.notification.controller.dto.EmailDetails;
import com.glaiss.notification.controller.dto.EmailValidador;
import com.glaiss.notification.domain.model.Email;
import com.glaiss.notification.domain.repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class EmailServiceImpl extends BaseServiceImpl<Email, UUID, EmailRepository> implements EmailService {

    private final static String ASSUNTO = "Redefinição de senha";
    private final static String CORPO_DO_EMAIL = "Seu código de acesso é %s e será válido pelos próximos 15 minutos";

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(EmailRepository repo,
                            JavaMailSender javaMailSender) {
        super(repo);
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(EmailDetails emailDetails) throws Exception {
        Email email = criarEmail(emailDetails.para());
        SimpleMailMessage simpleMailMessage = criarSimpleMailMessage(email);
        try {
            javaMailSender.send(simpleMailMessage);
            log.debug("sucesso no envio");
        } catch (Exception e) {
            log.error("Erro ao processar requisição", e);
            throw e;
        }
        salvar(email);
    }

    private Email criarEmail(String para) {
        String codigoAutenticacao = gerarCodigoAutenticacao();
        return new Email(para, ASSUNTO, String.format(CORPO_DO_EMAIL, codigoAutenticacao), codigoAutenticacao, LocalDateTime.now().plusMinutes(15L));
    }

    private String gerarCodigoAutenticacao() {
        int numeroAleatorio = 100000 + new Random().nextInt(900000);
        return String.valueOf(numeroAleatorio);
    }

    private SimpleMailMessage criarSimpleMailMessage(Email email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(email.getPara());
        simpleMailMessage.setSubject(ASSUNTO);
        simpleMailMessage.setText(email.getCorpo());
        return simpleMailMessage;
    }

    @Override
    public Email salvar(Email email){
        var emailEncontrado = repo.findByPara(email.getPara());
        emailEncontrado.ifPresent(value -> deletar(value.getId()));
        return super.salvar(email);
    }

    @Override
    public Boolean validarCodigoAutenticacao(EmailValidador emailValidador) {
        Optional<Email> emailEncontrado = repo.findByPara(emailValidador.usuario());
        if (emailEncontrado.isPresent()) {
            if (LocalDateTime.now().isBefore(emailEncontrado.get().getDataExpiracao())) {
                if (emailValidador.codigoValidacao().equals(emailEncontrado.get().getCodigoDeAutenticacao())) {
                    deletar(emailEncontrado.get().getId());
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
