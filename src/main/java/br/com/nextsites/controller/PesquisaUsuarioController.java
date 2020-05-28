package br.com.nextsites.controller;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 28/05/2020 - 14:35
 */

@Named
@ViewScoped
public class PesquisaUsuarioController {
    @Inject
    private UsuarioService usuarioService;

    @Getter @Setter
    List<UsuarioDto> usuarios;

    @PostConstruct
    public void init(){
        usuarios = usuarioService.getListaUsuarios();
    }
}
