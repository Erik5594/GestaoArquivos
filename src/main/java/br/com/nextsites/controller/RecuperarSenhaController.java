package br.com.nextsites.controller;

import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.file.FileUtil;
import br.com.nextsites.util.jsf.FacesUtil;
import br.com.nextsites.util.mail.Mailer;
import com.outjected.email.api.MailMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

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
    private Mailer mailer;

    @Inject
    private FileUtil fileUtil;

    private static final String TENTE_NOVAMENTE = "Tente novamente, caso persistir o problema, contate a administração do sistema!";

    public void inicializar() {
        if(StringUtils.isNotBlank(token)){
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
        if(validarEmail()){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            String data = simpleDateFormat.format(new Date());
            String token = usuario.getId().toString()+":"+data;

            String pagina = String.format("/RecuperarSenha.xhtml?token=%s", Base64.getEncoder().encodeToString(token.getBytes()));
            MailMessage message = mailer.novaMensagem();
            String template = "Olá "+usuario.getNome().split(" ")[0]+",<br/><br/>" +
                    "Para sua segurança não encaminhe este e-mail.<br/><br/>" +
                    "O link abaixo foi gerado para recuperação da sua senha.<br/>" +
                    "Ao clicar no link você será redirecionado á uma nova página para gerar uma nova senha.<br/><br/>" +
                    "<strong>"+fileUtil.getUrlDominio()+pagina+"</strong><br/><br/>" +
                    "<strong>Este link é válido somente hoje.<strong>";
            message.to(usuario.getEmail())
                    .subject("Recuperação de Senha")
                    .bodyHtml(template)
                    .send();


            FacesUtil.addInfoMessage("Foi enviado um e-mail com procedimentos para recuperação de senha!");
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
        return new String(Base64.getDecoder().decode(token));
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
