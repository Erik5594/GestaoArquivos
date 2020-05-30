package br.com.nextsites.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 00:27
 */
public class FacesUtil {
    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    public static boolean isNotPostback() {
        return !isPostback();
    }

    public static void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }

    public static void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    public static void addWarnMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }
}
