package br.com.nextsites.repository;

import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;
import br.com.nextsites.service.NegocioException;
import br.com.nextsites.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
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

    @Transactional
    public List<Arquivo> getArquivos(){
        return manager.createQuery("from Arquivo", Arquivo.class).getResultList();
    }

    @Transactional
    public List<Arquivo> getArquivosCategoria(Long idCategoria){
        return manager.createQuery("from Arquivo where categoria.id = :idCategoria", Arquivo.class)
                .setParameter("idCategoria", idCategoria)
                .getResultList();
    }

    @Transactional
    public List<Arquivo> getArquivos(Long idUsuario){
        try {
            return manager.createNativeQuery("select a.* " +
                    " from arquivo a " +
                    " inner join usuario_arquivo ua " +
                    " on a.id = ua.arquivo_id " +
                    " where ua.usuario_id = :idUsuario", Arquivo.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public List<Arquivo> getArquivosCategoria(Long idUsuario, Long idCategoria){
        try {
            return manager.createNativeQuery("select a.* " +
                    " from arquivo a " +
                    " inner join usuario_arquivo ua " +
                    " on a.id = ua.arquivo_id " +
                    " where ua.usuario_id = :idUsuario" +
                    " and a.categoria_id = :idCategoria", Arquivo.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idCategoria", idCategoria)
                    .getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public Arquivo salvar(Arquivo arquivo){
        try{
            Arquivo arquivoBanco = buscarNomeAndCategoria(arquivo.getNome(), arquivo.getCategoria().getId());
            if(arquivoBanco == null){
                arquivoBanco = this.manager.merge(arquivo);
            }
            return arquivoBanco;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public Arquivo editar(Arquivo arquivo){
        try{
            return this.manager.merge(arquivo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public Arquivo buscarNomeAndCategoria(String nomeArquivo, Long categoria){
        try{
            return this.manager.createQuery("from Arquivo where upper(nome) = :nome and upper(categoria.id) = :categoria", Arquivo.class)
                    .setParameter("nome", nomeArquivo.toUpperCase())
                    .setParameter("categoria", categoria)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Transactional
    public void incluirPermissao(Long idArquivo, Long idUsuario){
        try{
            if(!usuarioTemAcesso(idArquivo, idUsuario)){
                manager.createNativeQuery("insert into usuario_arquivo(arquivo_id, usuario_id) " +
                        "values(:idArquivo, :idUsuario)")
                        .setParameter("idArquivo", idArquivo)
                        .setParameter("idUsuario", idUsuario)
                        .executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public void removerTodasPermissao(Long idArquivo){
        try{
            manager.createNativeQuery("delete from usuario_arquivo where arquivo_id = :idArquivo")
                    .setParameter("idArquivo", idArquivo)
                    .executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public boolean usuarioTemAcesso(Long idArquivo, Long idUsuario){
        try {
            return (int) manager.createNativeQuery("select 1 from usuario_arquivo where arquivo_id = :idArquivo and usuario_id = :idUsuario")
                    .setParameter("idArquivo", idArquivo)
                    .setParameter("idUsuario", idUsuario)
                    .getSingleResult() > 0;
        } catch (NoResultException e){
            return false;
        }
    }

    @Transactional
    public List<Usuario> getUsuariosComAcesso(Long idArquivo){
        return manager.createNativeQuery("select u.* " +
                " from usuario u " +
                " inner join usuario_arquivo ua " +
                " on u.id = ua.usuario_id " +
                " where ua.arquivo_id = :idArquivo" +
                " and u.status = :status " +
                " and u.nivel_id = :nivel ", Usuario.class)
                .setParameter("idArquivo", idArquivo)
                .setParameter("status", 0)
                .setParameter("nivel", 2)
                .getResultList();
    }

    @Transactional
    public List<Usuario> getUsuariosSemPermissao(Long idArquivo){
        return manager.createNativeQuery("select u.* " +
                " from usuario u " +
                " where not exists " +
                " (select 1 " +
                " from arquivo a " +
                " inner join usuario_arquivo ua " +
                " on a.id = ua.arquivo_id " +
                " where ua.usuario_id = u.id " +
                " and a.id = :idArquivo) " +
                " and u.status = :situacao " +
                " and u.nivel_id = :nivel ", Usuario.class)
                .setParameter("idArquivo", idArquivo)
                .setParameter("situacao", 0)
                .setParameter("nivel", 2)
                .getResultList();
    }

    @Transactional
    public void remover(Arquivo arquivo) throws NegocioException {
        try {
            removerTodasPermissao(arquivo.getId());

            arquivo = porId(arquivo.getId());
            manager.remove(arquivo);
            manager.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Arquivo não pode ser excluído.");
        }
    }

    @Transactional
    public Arquivo porId(Long id) {
        return this.manager.find(Arquivo.class, id);
    }

    @Transactional
    public List<Arquivo> porNome(String nome, Long idUsuario) {
        String query1 = " select a.* from arquivo a " +
                " where upper(a.nome) ilike :pesquisa ";

        String query2 = query1 +
                " and exists (select 1 " +
                " from usuario_arquivo ua " +
                " where ua.arquivo_id = a.id " +
                " and ua.usuario_id = :idUsuario) ";

        try {
            if(idUsuario != null && idUsuario > 0l){
                return this.manager.createNativeQuery(query2, Arquivo.class)
                        .setParameter("pesquisa", "%"+nome.toUpperCase()+"%")
                        .setParameter("idUsuario", idUsuario)
                        .getResultList();
            }else{
                return this.manager.createNativeQuery(query1, Arquivo.class)
                        .setParameter("pesquisa", "%"+nome.toUpperCase()+"%")
                        .getResultList();
            }
        } catch (NoResultException e) {
            return null;
        }
    }
}
