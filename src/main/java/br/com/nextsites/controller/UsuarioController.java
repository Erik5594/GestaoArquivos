package br.com.nextsites.controller;

import br.com.nextsites.dto.GrupoDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.GrupoService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 22/05/2020 - 17:15
 */

@Named
@ViewScoped
public class UsuarioController {

    private static final String TITULO = "Cadastro de Usuário: %s";
    private static final String ERRO_INTERNO = String.format(TITULO, "Ocorreu um erro interno. Contate a administração do sistema!");
    private static final String SUCESSO = String.format(TITULO, "Cadastro realizado com sucesso!");

    @Getter @Setter
    private UsuarioDto usuario;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private GrupoService nivelService;

    @Getter @Setter
    private List<GrupoDto> niveis;

    @Getter @Setter
    private String senhaAtual;

    public UsuarioController() {
        limpar();
    }

    public void inicializar() {
        if(usuario == null){
            limpar();
        }
        niveis = nivelService.getGrupos();
    }

    public void salvar(){
        List<String> validacoes = usuarioService.getValidar(usuario, TITULO);
        if (validacoes == null || validacoes.isEmpty()) {
            usuarioService.salvar(usuario);
            limpar();
            FacesUtil.addInfoMessage(SUCESSO);
        } else {
            imprimirValidacoes(validacoes);
        }
    }

    private void limpar(){
        usuario = new UsuarioDto();
        niveis = null;
    }

    private void imprimirValidacoes(List<String> validcoes){
        for(String validacao : validcoes){
            FacesUtil.addErrorMessage(validacao);
        }
    }

    public boolean isEditando(){
        return this.usuario.getId() != null;
    }

    public void resetarSenha(){
        usuario.setSenha("1234");
        usuarioService.salvar(usuario);
        FacesUtil.addInfoMessage("Senha resetada com sucesso!");
    }
}
