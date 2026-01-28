# Projeto de Notificação

Este projeto é um microserviço de notificação por e-mail construído com Spring Boot.

## Visão Geral

O serviço expõe uma API REST para enviar e-mails. Ele se integra a um servidor Eureka para descoberta de serviços e
utiliza um banco de dados PostgreSQL para persistir informações sobre os e-mails enviados. A segurança é fornecida por
meio de uma chave de API.

## Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.4.2**
* **Spring Cloud**
* **Maven**
* **PostgreSQL**
* **Flyway**
* **Eureka**
* **Docker** 

## Endpoints da API

* `POST /notificacao-por-email`: Envia um e-mail.
    * **Corpo da Solicitação:**
      ```json
      {
        "para": "destinatario@example.com",
        "assunto": "Assunto do E-mail",
        "corpo": "Corpo da mensagem do e-mail."
      }
      ```
* `POST /notificacao-por-email/validar-codigo`: Valida um código de autenticação enviado por e-mail.
    * **Corpo da Solicitação:**
      ```json
      {
        "email": "destinatario@example.com",
        "codigo": "123456"
      }
      ```

## Segurança

O acesso à API é protegido por uma chave de API que deve ser enviada no cabeçalho `X-CHAVE-DE-ACESSO`.

## Configuração

O projeto usa `application.yml` para configuração. Ele define perfis para os ambientes `dev` e `prod`.

### Variáveis de Ambiente (Produção)

* `DB_URL`: URL do banco de dados PostgreSQL.
* `USER_DB`: Nome de usuário do banco de dados.
* `PASSWORD_DB`: Senha do banco de dados.
* `MAIL_HOST`: Host do servidor de e-mail.
* `MAIL_PORT`: Porta do servidor de e-mail.
* `MAIL_USERNAME`: Nome de usuário do servidor de e-mail.
* `MAIL_PASSWORD`: Senha do servidor de e-mail.
* `CHAVES_DE_ACESSO`: Chaves de API válidas, separadas por vírgula.
* `URL_DISCOVERY`: URL do servidor Eureka.
* `PORT`: Porta em que o serviço será executado.
* `LOGSTASH`: Destino do Logstash.

## Build e Execução

### Com Maven

Para construir o projeto, execute:

```bash
./mvnw clean install
```

Para executar o projeto, execute:

```bash
java -jar target/notification-0.6.1.jar
```

### Com Docker

Para construir a imagem do Docker, execute:

```bash
docker build -t notification-service .
```

Para executar o container do Docker, execute:

```bash
docker run -p 8194:8194 notification-service
```
