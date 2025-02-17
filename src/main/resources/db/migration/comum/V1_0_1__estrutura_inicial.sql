CREATE TABLE EMAILS (
	ID                      UUID                       NOT NULL,
	PARA                    VARCHAR(250)               NOT NULL,
	ASSUNTO                 VARCHAR(250)               NOT NULL,
	CORPO                   VARCHAR(250)               NOT NULL,
	CODIGO_DE_AUTENTICACAO  VARCHAR(250)               NOT NULL,
	DATA_EXPIRACAO          timestamp with time zone   NOT NULL,
	CREATED_DATE            timestamp with time zone   NULL,
	MODIFIED_DATE           timestamp with time zone   NULL,
	CREATED_BY              VARCHAR(100)               NULL,
	MODIFIED_BY             VARCHAR(100)               NULL,
	CONSTRAINT PK_EMAILS PRIMARY KEY ( ID )
 );