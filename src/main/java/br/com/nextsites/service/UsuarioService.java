package br.com.nextsites.service;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.util.jpa.Transactional;

import javax.inject.Inject;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 24/05/2020 - 09:17
 */
public class UsuarioService {

    @Inject
    private Usuarios usuarioDao;

    @Transactional
    public UsuarioDto getUsuarioPorEmail(String email){
        return new UsuarioDto(usuarioDao.porEmail(email));
    }

    @Transactional
    public UsuarioDto getUsuarioById(Long id){
        return new UsuarioDto(usuarioDao.porId(id));
    }
}
