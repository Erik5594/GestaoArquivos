package br.com.nextsites.repository;

import br.com.nextsites.model.Arquivo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 26/05/2020 - 20:54
 */
public class Arquivos  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    public List<Arquivo> getArquivos(){
        return manager.createQuery("from Arquivo", Arquivo.class).getResultList();
    }

    public Arquivo porId(Long id) {
        return this.manager.find(Arquivo.class, id);
    }

    public void salvar(Arquivo arquivo){
        this.manager.merge(arquivo);
    }
}
