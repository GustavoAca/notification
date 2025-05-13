package com.glaiss.notification.controller;

import com.glaiss.notification.controller.dto.EmailDetails;
import com.glaiss.notification.controller.dto.EmailValidador;
import com.glaiss.notification.domain.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacao-por-email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public void sendMail(@RequestBody EmailDetails details) throws Exception {
        emailService.sendMail(details);
    }

    @PostMapping("/validar-codigo")
    public Boolean validarCodigoAutenticacao(@RequestBody EmailValidador emailValidador) {
        return emailService.validarCodigoAutenticacao(emailValidador);
    }
}
