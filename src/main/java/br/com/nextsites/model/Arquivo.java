package br.com.nextsites.model;

import br.com.nextsites.dto.ArquivoDto;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    @Lob
    private byte[] conteudo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvio;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_arquivo", joinColumns = @JoinColumn(name="arquivo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuarios;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Arquivo(ArquivoDto arquivoDto) {
        this.id = arquivoDto.getId();
        this.nome = arquivoDto.getNome();
        this.conteudo = arquivoDto.getConteudo();
        this.dataEnvio = arquivoDto.getDataEnvio();
        this.categoria = new Categoria(arquivoDto.getCategoriaDto());
    }

    public Arquivo() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arquivo other = (Arquivo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
