package br.com.nextsites.util.mail;

import com.outjected.email.api.MailMessage;
import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.MailMessageImpl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 31/05/2020 - 20:04
 */

@RequestScoped
public class Mailer implements Serializable{

    private static final long serialVersionUID = 1L;

    @Inject
    private SessionConfig config;

    public MailMessage novaMensagem() {
        return new MailMessageImpl(this.config);
    }
}
