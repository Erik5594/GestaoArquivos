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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 22/05/2020 - 17:15
 */

@Named
@ViewScoped
public class RecuperarSenhaController {

    @Getter @Setter
    private UsuarioDto usuario;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String token;

    private Calendar dataLimite;
    private Long idUsuario;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private ExternalContext externalContext;

    private static final String TENTE_NOVAMENTE = "Tente novamente, caso persistir o problema, contate a administração do sistema!";

    public void inicializar() {
        if(StringUtils.isNotBlank(token)){
            System.out.println("token: "+token);
            String tokenDescriptografado = descriptografarToken(token);
            extrairInformacoes(tokenDescriptografado);
            if(!dadosTokenEhValido()){
                limpar();
            }
        }
    }

    public String salvar(){
        if(usuario != null && usuario.getId() != null){
            usuarioService.salvar(usuario);
            FacesUtil.addInfoMessage("Senha recuperada com sucesso!");
            return "/Login.xhtml";
        }else{
            FacesUtil.addErrorMessage("Ocorreu um erro ao recuperar a senha!");
            return "/";
        }
    }

    public void enviarEmail(){
        try {
            if(validarEmail()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
                String data = simpleDateFormat.format(new Date());
                externalContext.redirect(String.format("/RecuperarSenha.xhtml?token=%s:%s",usuario.getId().toString(),data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isInformarEmail(){
        return StringUtils.isBlank(token);
    }

    private boolean validarEmail(){
        boolean retorno = true;
        if(StringUtils.isBlank(email)){
            retorno = false;
            FacesUtil.addErrorMessage("Deve ser informado um e-mail para recuperação de senha!");
        }else if(!email.contains("@")) {
            retorno = false;
            FacesUtil.addErrorMessage("Deve ser informado um e-mail válido para recuperação de senha!");
        }else{
            usuario = usuarioService.getUsuarioPorEmail(email);
            if(usuario == null || usuario.getId() == null){
                retorno = false;
                FacesUtil.addErrorMessage(String.format("Não foi encontrado um Usuário vinculado ao e-mail: %s",email));
            }
        }
        return retorno;
    }

    private void limpar(){
        usuario = new UsuarioDto();
        email = "";
        token = "";
        dataLimite = null;
        idUsuario = null;
    }

    private boolean dadosTokenEhValido(){
        boolean retorno = true;
        if(idUsuario == null || idUsuario < 1l || dataLimite == null || usuario == null || usuario.getId() == null){
            retorno = false;
            FacesUtil.addErrorMessage(String.format("Link de recuperação de senha é inválido. %s", TENTE_NOVAMENTE));
        }
        if(dataLimite != null && dataLimite.compareTo(Calendar.getInstance()) < 0){
            retorno = false;
            FacesUtil.addErrorMessage(String.format("Link de recuperação de senha já experiou. %s", TENTE_NOVAMENTE));
        }
        return retorno;
    }

    private String descriptografarToken(String token){
        return token;
    }

    private void extrairInformacoes(String tokenDescriptografado){
        String dados[] = tokenDescriptografado.split(":");

        idUsuario = Long.parseLong(dados[0]);
        usuario = usuarioService.getUsuarioById(idUsuario);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
        try {
            dataLimite = Calendar.getInstance();
            dataLimite.setTime(simpleDateFormat.parse(dados[1]));
            dataLimite.set(Calendar.HOUR, 23);
            dataLimite.set(Calendar.MINUTE, 59);
            dataLimite.set(Calendar.SECOND, 59);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
