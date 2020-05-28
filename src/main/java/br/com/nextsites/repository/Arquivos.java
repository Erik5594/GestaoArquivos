package br.com.nextsites.repository;

import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;

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

    public List<Arquivo> getArquivos(){
        return manager.createQuery("from Arquivo", Arquivo.class).getResultList();
    }

    public Arquivo porId(Long id) {
        return this.manager.find(Arquivo.class, id);
    }

    public void salvar(Arquivo arquivo){
        if(arquivo.getUsuarios() != null){
            List<Usuario> usuarios = new ArrayList<>();
            for(Usuario usuario : arquivo.getUsuarios()){
                usuarios.add(usuarioDao.porId(usuario.getId()));
            }
            arquivo.setUsuarios(usuarios);
        }
        try{
            this.manager.merge(arquivo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
