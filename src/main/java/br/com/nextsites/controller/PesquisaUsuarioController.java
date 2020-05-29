package br.com.nextsites.controller;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
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

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String nome;

    @Getter @Setter
    private UsuarioDto usuarioDto;

    @PostConstruct
    public void init(){
        limpar();
        usuarios = usuarioService.getListaUsuarios();
    }

    public void pesquisarUsuarios(){
        if(StringUtils.isNotBlank(nome) || StringUtils.isNotBlank(email)){
            usuarios = usuarioService.getUsuarioFiltrados(nome, email);
        }else{
            usuarios = usuarioService.getListaUsuarios();
        }
    }

    public void remover(){
        if(usuarioDto != null && usuarioDto.getId() != null){
            usuarioService.remover(usuarioDto);
            init();
            FacesUtil.addInfoMessage("Usuario removido com sucesso!");
        }else{
            FacesUtil.addErrorMessage("Deve ser selecionado um usuário!");
        }
    }

    private void limpar(){
        email = "";
        nome = "";
        usuarioDto = new UsuarioDto();
        usuarios = new ArrayList<>();
    }
}
