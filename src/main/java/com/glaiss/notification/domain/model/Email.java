package com.glaiss.notification.domain.model;

import com.glaiss.core.common.builder.IBuilder;
import com.glaiss.core.domain.model.EntityAbstract;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "emails")
@Entity
public class Email extends EntityAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String para;
    private String assunto;
    private String corpo;
    @Column(name = "codigo_de_autenticacao")
    private String codigoDeAutenticacao;
    @Column(name = "dataExpiracao")
    private LocalDateTime dataExpiracao;

    // Construtor protegido para JPA
    protected Email() {
    }

    public Email(String para, String assunto, String corpo, String codigoDeAutenticacao, LocalDateTime dataExpiracao) {
        this.para = para;
        this.assunto = assunto;
        this.corpo = corpo;
        this.codigoDeAutenticacao = codigoDeAutenticacao;
        this.dataExpiracao = dataExpiracao;
    }

    // Construtor privado para o Builder
    private Email(EmailBuilder builder) {
        this.id = builder.id;
        this.para = builder.para;
        this.assunto = builder.assunto;
        this.corpo = builder.corpo;
        this.codigoDeAutenticacao = builder.codigoDeAutenticacao;
        this.dataExpiracao = builder.dataExpiracao;
        this.setCreatedDate(builder.createdDate);
        this.setCreatedBy(builder.createdBy);
        this.setModifiedDate(builder.modifiedDate);
        this.setModifiedBy(builder.modifiedBy);
        this.setVersion(builder.version);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getCodigoDeAutenticacao() {
        return codigoDeAutenticacao;
    }

    public void setCodigoDeAutenticacao(String codigoDeAutenticacao) {
        this.codigoDeAutenticacao = codigoDeAutenticacao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    // Método estático para iniciar o builder
    public static EmailBuilder builder() {
        return new EmailBuilder();
    }

    // Implementação manual do Builder
    public static final class EmailBuilder implements IBuilder<Email> {
        private UUID id;
        private String para;
        private String assunto;
        private String corpo;
        private String codigoDeAutenticacao;
        private LocalDateTime dataExpiracao;
        private LocalDateTime createdDate;
        private String createdBy;
        private LocalDateTime modifiedDate;
        private String modifiedBy;
        private Long version;

        private EmailBuilder() {
        }

        public EmailBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public EmailBuilder para(String para) {
            this.para = para;
            return this;
        }

        public EmailBuilder assunto(String assunto) {
            this.assunto = assunto;
            return this;
        }

        public EmailBuilder corpo(String corpo) {
            this.corpo = corpo;
            return this;
        }

        public EmailBuilder codigoDeAutenticacao(String codigoDeAutenticacao) {
            this.codigoDeAutenticacao = codigoDeAutenticacao;
            return this;
        }

        public EmailBuilder dataExpiracao(LocalDateTime dataExpiracao) {
            this.dataExpiracao = dataExpiracao;
            return this;
        }

        public EmailBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public EmailBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public EmailBuilder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public EmailBuilder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public EmailBuilder version(Long version) {
            this.version = version;
            return this;
        }

        @Override
        public Email build() {
            return new Email(this);
        }
    }
}
