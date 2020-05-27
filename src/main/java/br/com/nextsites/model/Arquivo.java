package br.com.nextsites.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 26/05/2020 - 20:04
 */
@Entity
@Table(name = "arquivo")
public @Data class Arquivo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 200)
    private String diretorio;

    @Lob
    private byte[] conteudo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvio;

    @ManyToMany(mappedBy = "arquivos")
    private List<Usuario> usuarios;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arquivo arquivo = (Arquivo) o;
        return Objects.equals(id, arquivo.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
