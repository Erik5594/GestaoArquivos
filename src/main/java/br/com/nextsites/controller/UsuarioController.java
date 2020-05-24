package br.com.nextsites.controller;

import br.com.nextsites.dto.GrupoDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.model.Grupo;
import br.com.nextsites.repository.Usuarios;
import br.com.nextsites.service.GrupoService;
import br.com.nextsites.service.UsuarioService;
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

    @Inject
    private UsuarioDto usuario;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private GrupoService grupoService;

    @Getter @Setter
    private List<GrupoDto> niveis;

    @Getter @Setter
    private GrupoDto nivelSelecionado;

    @PostConstruct
    public void init(){
        niveis = grupoService.getGrupos();
        if(niveis != null && !niveis.isEmpty()){
            for(GrupoDto grupo : niveis){
                System.out.println(grupo);
            }
        }
    }

    public void salvar(){
        //throw new RuntimeException("Teste");
        System.out.println(usuario);
        System.out.println(nivelSelecionado);
        sendMensagem(SUCESSO, FacesMessage.SEVERITY_INFO);
        usuarioService.getUsuarioById(1l);
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
