package br.com.nextsites.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 31/05/2020 - 21:06
 */

@Entity
@Table(name = "email_config")
public @Data class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    @Column(nullable=false, length=40)
    private String serverHost;

    @Column(nullable=false)
    private int serverPort;

    @Column(nullable=false)
    private boolean ssl;

    @Column(nullable=false)
    private boolean autenticacao;

    @Column
    private String password;

    @Column(nullable = false)
    private boolean ativo;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmailConfig other = (EmailConfig) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
}
