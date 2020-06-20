package br.com.nextsites.repository;

import br.com.nextsites.model.Categoria;
import br.com.nextsites.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 08/06/2020 - 19:51
 */
public class Categorias implements Serializable{
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    @Transactional
    public List<Categoria> buscarTodas(){
        return manager.createQuery("from Categoria", Categoria.class).getResultList();
    }

    @Transactional
    public Categoria salvar(Categoria categoria){
        Long idCategoriaPai = categoria.getCategoriaPai() != null ? categoria.getCategoriaPai().getId():null;
        if(categoria.getId() == null
                && existeCategoria(categoria.getNomeCategoria(), idCategoriaPai)){
            categoria = findNomeAndIdPai(categoria.getNomeCategoria(), idCategoriaPai);
        }

        return manager.merge(categoria);
    }

    @Transactional
    public void deletar(Long idCategoria){
        Categoria categoria = findById(idCategoria);
        manager.remove(categoria);
    }

    @Transactional
    public Categoria editarNome(Long idCategoria, String novoNome){
        Categoria categoria = findById(idCategoria);
        categoria.setNomeCategoria(novoNome);
        return manager.merge(categoria);
    }

    @Transactional
    public boolean existeCategoria(String nomeCategoria, Long idCategoriaPai){
        try{
            return findNomeAndIdPai(nomeCategoria, idCategoriaPai) != null;
        }catch (NoResultException e){
            return false;
        }
    }

    @Transactional
    public Categoria findNomeAndIdPai(String nomeCategoria, Long idCategoriaPai){
        try{
            return manager.createQuery("from Categoria where upper(nomeCategoria) = :nomeCategoria " +
                    " and categoriaPai.id = :idCategoriaPai", Categoria.class)
                    .setParameter("nomeCategoria", nomeCategoria.toUpperCase())
                    .setParameter("idCategoriaPai", idCategoriaPai)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public Categoria findById(Long idCategoria){
        try{
            return manager.find(Categoria.class, idCategoria);
        }catch (NoResultException e){
            return null;
        }
    }

}
