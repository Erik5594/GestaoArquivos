package br.com.nextsites.service;

import br.com.nextsites.dto.EmailConfigDto;
import br.com.nextsites.repository.EmailConfigs;

import javax.inject.Inject;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 31/05/2020 - 21:13
 */
public class ConfigEmailService {
    @Inject
    private EmailConfigs emailConfigsDao;

    public EmailConfigDto getConfigEmail(){
        return new EmailConfigDto(emailConfigsDao.getConfiguracao());
    }
}
