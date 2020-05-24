package br.com.nextsites.repository;

import br.com.nextsites.model.Grupo;
import br.com.nextsites.model.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 24/05/2020 - 09:14
 */
public class Grupos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    public List<Grupo> getGrupos(){
        return manager.createQuery("from Grupo", Grupo.class).getResultList();
    }

    public Grupo porId(Long id) {
        return this.manager.find(Grupo.class, id);
    }
}
