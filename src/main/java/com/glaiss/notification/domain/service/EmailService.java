package com.glaiss.notification.domain.service;

import com.glaiss.core.domain.service.BaseService;
import com.glaiss.notification.controller.dto.EmailDetails;
import com.glaiss.notification.domain.model.Email;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface EmailService extends BaseService<Email, UUID>  {

    void sendMail(EmailDetails emailDetails) throws Exception;

}
