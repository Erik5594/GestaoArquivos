package br.com.nextsites.repository;

import br.com.nextsites.model.Usuario;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 14:07
 */
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    @Inject
    private Grupos grupos;

    public Usuario porId(Long id) {
        return this.manager.find(Usuario.class, id);
    }

    public Usuario porEmail(String email) {
        Usuario usuario = null;

        try {
            usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
                    .setParameter("email", email.toLowerCase()).getSingleResult();
        } catch (NoResultException e) {
            // nenhum usuário encontrado com o e-mail informado
        }

        return usuario;
    }

    public void salvar(Usuario usuario){
        usuario.setNivel(grupos.porId(usuario.getNivel().getId()));
        manager.merge(usuario);
    }

    public List<Usuario> listarClientes() {
        List<Usuario> usuarios = null;

        try {
            usuarios = this.manager.createQuery("from Usuario where lower(nivel.nome) = :nivel", Usuario.class)
                    .setParameter("nivel", "cliente".toLowerCase()).getResultList();
        } catch (NoResultException e) {
            // nenhum usuário encontrado com o e-mail informado
        }

        return usuarios;
    }

}