package br.com.nextsites.controller;

import br.com.nextsites.dto.GrupoDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.model.Grupo;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.service.GrupoService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.Consts;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
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
        usuario.setNivel(nivelSelecionado.getId());
        List<String> validacoes = usuarioService.getValidar(usuario, TITULO);
        if (validacoes == null || validacoes.isEmpty()) {
            usuarioService.salvar(usuario);
            limpar();
            sendMensagem(SUCESSO, FacesMessage.SEVERITY_INFO);
        } else {
            imprimirValidacoes(validacoes);
        }
    }

    public void sendMensagem(String mensagem, FacesMessage.Severity nivel){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nivel, mensagem, null));
    }

    private void limpar(){
        usuario = new UsuarioDto();
        niveis = null;
        nivelSelecionado = new GrupoDto();
    }

    private void imprimirValidacoes(List<String> validcoes){
        for(String validacao : validcoes){
            sendMensagem(validacao, FacesMessage.SEVERITY_FATAL);
        }
    }
}