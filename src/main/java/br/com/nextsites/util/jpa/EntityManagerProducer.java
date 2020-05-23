package br.com.nextsites.util.jpa;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 14:00
 */

@ApplicationScoped
public class EntityManagerProducer {
    private EntityManagerFactory factory;

    public EntityManagerProducer() {
        factory = Persistence.createEntityManagerFactory("GestaoArqPU");
    }

    @Produces @RequestScoped
    public Session createEntityManager() {
        return (Session) factory.createEntityManager();
    }

    public void closeEntityManager(@Disposes Session manager) {
        manager.close();
    }
}
