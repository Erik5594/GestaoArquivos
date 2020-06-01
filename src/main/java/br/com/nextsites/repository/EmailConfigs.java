package br.com.nextsites.repository;

import br.com.nextsites.model.EmailConfig;
import br.com.nextsites.service.NegocioException;
import br.com.nextsites.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.Serializable;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 31/05/2020 - 21:14
 */
public class EmailConfigs implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    @Transactional
    public EmailConfig getConfiguracao(){
        try{
            return this.manager.createQuery("from EmailConfig where ativo = true", EmailConfig.class).getSingleResult();
        }catch (NoResultException e){
            throw new NegocioException("Não foi encontrado configuração de envio de emails");
        }
    }
}
