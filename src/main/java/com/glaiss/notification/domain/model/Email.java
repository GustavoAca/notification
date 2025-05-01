package com.glaiss.notification.domain.model;

import com.glaiss.core.domain.model.EntityAbstract;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
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

    public Email(String para, String assunto, String corpo, String codigoDeAutenticacao, LocalDateTime dataExpiracao) {
        this.para = para;
        this.assunto = assunto;
        this.corpo = corpo;
        this.codigoDeAutenticacao = codigoDeAutenticacao;
        this.dataExpiracao = dataExpiracao;
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
}
