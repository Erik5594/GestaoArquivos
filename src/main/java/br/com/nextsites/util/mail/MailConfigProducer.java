package br.com.nextsites.util.mail;

import br.com.nextsites.dto.EmailConfigDto;
import br.com.nextsites.service.ConfigEmailService;
import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.SimpleMailConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 31/05/2020 - 20:06
 */
public class MailConfigProducer {

    @Inject
    private ConfigEmailService configEmailService;

    @Produces
    @ApplicationScoped
    public SessionConfig getMailConfig() {
        EmailConfigDto configDto = configEmailService.getConfigEmail();

        SimpleMailConfig config = new SimpleMailConfig();
        config.setServerHost(configDto.getServerHost());
        config.setServerPort(configDto.getServerPort());
        config.setEnableSsl(configDto.isSsl());
        config.setAuth(configDto.isAutenticacao());
        config.setUsername(configDto.getUsername());
        config.setPassword(configDto.getPassword());

        return config;
    }
}
