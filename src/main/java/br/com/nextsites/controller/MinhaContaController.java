package br.com.nextsites.controller;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 22/05/2020 - 17:15
 */

@Named
@ViewScoped
public class MinhaContaController {

    @Getter @Setter
    private UsuarioDto usuario;

    @Inject
    private UsuarioService usuarioService;

    @Getter @Setter
    private String senhaAtual;

    private String senhaAtualSalvada;

    @Inject
    private ExternalContext externalContext;

    private static final String TITULO = "Minha conta: %s";
    private static final String SUCESSO = String.format(TITULO, "Cadastro realizado com sucesso!");

    public void inicializar() {
        if(usuario == null){
            try {
                externalContext.redirect("index.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            senhaAtualSalvada = usuarioService.getSenhaUsuario(usuario.getId());
            if(StringUtils.isBlank(senhaAtualSalvada)){
                FacesUtil.addInfoMessage("Ocorreu um erro ao recuperar senha do usuário!");
            }
        }
    }

    public String salvar(){
        List<String> validacoes = usuarioService.getValidar(usuario, TITULO);
        if (validacoes == null || validacoes.isEmpty()) {
            if(senhaAtualConfere()){
                usuarioService.salvar(usuario);
                FacesUtil.addInfoMessage(SUCESSO);
                return "index.xhtml";
            }else{
                FacesUtil.addErrorMessage("Senha atual informada diverge da senha atual do usuário!");
            }
        } else {
            imprimirValidacoes(validacoes);
        }
        return "";
    }

    private boolean senhaAtualConfere(){
        return StringUtils.isNotBlank(senhaAtualSalvada) && senhaAtualSalvada.equals(senhaAtual);
    }

    private void imprimirValidacoes(List<String> validcoes){
        for(String validacao : validcoes){
            FacesUtil.addErrorMessage(validacao);
        }
    }
}
