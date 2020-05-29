package br.com.nextsites.repository;

import br.com.nextsites.model.StatusUsuario;
import br.com.nextsites.model.Usuario;
import br.com.nextsites.service.NegocioException;
import br.com.nextsites.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

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

    @Transactional
    public Usuario porId(Long id) {
        return this.manager.find(Usuario.class, id);
    }

    @Transactional
    public Usuario porEmail(String email) {
        Usuario usuario = null;

        try {
            usuario = this.manager.createQuery("from Usuario where lower(email) = :email and status = :status", Usuario.class)
                    .setParameter("status",StatusUsuario.ATIVO)
                    .setParameter("email", email.toLowerCase()).getSingleResult();
        } catch (NoResultException e) {
            // nenhum usuário encontrado com o e-mail informado
        }

        return usuario;
    }

    @Transactional
    public void salvar(Usuario usuario){
        usuario.setNivel(grupos.porId(usuario.getNivel().getId()));
        manager.merge(usuario);
    }

    @Transactional
    public List<Usuario> listarClientes() {
        List<Usuario> usuarios = null;

        try {
            usuarios = this.manager.createQuery("from Usuario where lower(nivel.nome) = :nivel and status = :status", Usuario.class)
                    .setParameter("status",StatusUsuario.ATIVO)
                    .setParameter("nivel", "cliente".toLowerCase()).getResultList();
        } catch (NoResultException e) {
            // nenhum usuário encontrado com o e-mail informado
        }

        return usuarios;
    }

    @Transactional
    public List<Usuario> listarUsuarios() {
        return this.manager.createQuery("from Usuario where status = :status", Usuario.class)
                .setParameter("status",StatusUsuario.ATIVO)
                .getResultList();
    }

    @Transactional
    public List<Usuario> filtrados(String nome, String email){
        Session session = manager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("status", StatusUsuario.ATIVO));
        if (StringUtils.isNotBlank(email)) {
            criteria.add(Restrictions.eq("email", email));
        }

        if (StringUtils.isNotBlank(nome)) {
            criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));
        }

        return criteria.addOrder(Order.asc("nome")).list();
    }

    @Transactional
    public void remover(Usuario usuario) throws NegocioException {
        try {
            usuario = porId(usuario.getId());
            usuario.setStatus(StatusUsuario.INATIVO);
            manager.merge(usuario);
            manager.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Usuário não pode ser excluído.");
        }
    }

}