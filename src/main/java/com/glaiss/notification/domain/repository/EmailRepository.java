package com.glaiss.notification.domain.repository;

import com.glaiss.core.domain.repository.BaseRepository;
import com.glaiss.notification.domain.model.Email;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends BaseRepository<Email, UUID> {

    Optional<Email> findByPara(String para);
}
