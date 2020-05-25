package br.com.nextsites.controller;

import br.com.nextsites.dto.GrupoDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.model.Grupo;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.service.GrupoService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

@Named(value = "usuarioController")
@ViewScoped
public class UsuarioController {

    private static final String TITULO = "Cadastro de Usuário: %s";
    private static final String ERRO_INTERNO = String.format(TITULO, "Ocorreu um erro interno. Contate a administração do sistema!");
    private static final String SUCESSO = String.format(TITULO, "Cadastro realizado com sucesso!");

    @Inject @Getter @Setter
    private UsuarioDto usuario;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private GrupoService nivelService;

    @Getter @Setter
    private List<GrupoDto> niveis;

    @Getter @Setter
    private GrupoDto nivelSelecionado;

    public void inicializar() {
        if (FacesUtil.isNotPostback()) {
            if(usuario == null){
                limpar();
            }
            niveis = nivelService.getGrupos();
        }
    }

    public void salvar(){
        limpar();
        sendMensagem(SUCESSO, FacesMessage.SEVERITY_INFO);
    }

    public void sendMensagem(String mensagem, FacesMessage.Severity nivel){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nivel, mensagem, null));
    }

    private void limpar(){
        usuario = new UsuarioDto();
        niveis = null;
        nivelSelecionado = null;
    }
}
