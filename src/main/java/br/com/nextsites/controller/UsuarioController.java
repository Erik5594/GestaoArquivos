package br.com.nextsites.controller;

import br.com.nextsites.dto.UsuarioDto;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

    @Inject
    private UsuarioDto usuario;

    public void salvar(){
        //throw new RuntimeException("Teste");
        System.out.println(usuario);
        sendMensagem(SUCESSO, FacesMessage.SEVERITY_INFO);
    }

    public void sendMensagem(String mensagem, FacesMessage.Severity nivel){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nivel, mensagem, null));
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }
}
