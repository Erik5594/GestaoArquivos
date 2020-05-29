package br.com.nextsites.repository;

import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;
import br.com.nextsites.util.jpa.Transactional;

import javax.faces.context.ExceptionHandler;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Inject
    private Usuarios usuarioDao;

    @Transactional
    public List<Arquivo> getArquivos(){
        return manager.createQuery("from Arquivo", Arquivo.class).getResultList();
    }

    @Transactional
    public Arquivo porId(Long id) {
        return this.manager.find(Arquivo.class, id);
    }

    @Transactional
    public Arquivo salvar(Arquivo arquivo){
        try{
            return this.manager.merge(arquivo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public void incluirPermissao(Long idArquivo, Long idUsuario){
        try{
            manager.createNativeQuery("insert into usuario_arquivo(arquivo_id, usuario_id) " +
                    "values(:idArquivo, :idUsuario)")
                    .setParameter("idArquivo", idArquivo)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public void removerPermissao(Long idArquivo, Long idUsuario){
        try{
            manager.createNativeQuery("delete from usuario_arquivo where arquivo_id = :idArquivo and usuario_id = :idUsuario)")
                    .setParameter("idArquivo", idArquivo)
                    .setParameter("idUsuario", idUsuario)
                    .executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
