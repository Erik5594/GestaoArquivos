package br.com.nextsites.dto;

import br.com.nextsites.model.EmailConfig;
import lombok.Data;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 31/05/2020 - 21:17
 */

public @Data class EmailConfigDto {

    private String username;
    private String serverHost;
    private int serverPort;
    private boolean ssl;
    private boolean autenticacao;
    private String password;

    public EmailConfigDto() {
    }

    public EmailConfigDto(EmailConfig emailConfig) {
        this.username = emailConfig.getUsername();
        this.serverHost = emailConfig.getServerHost();
        this.serverPort = emailConfig.getServerPort();
        this.ssl = emailConfig.isSsl();
        this.autenticacao = emailConfig.isAutenticacao();
        this.password = emailConfig.getPassword();
    }
}
