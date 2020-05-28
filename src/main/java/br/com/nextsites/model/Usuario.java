package br.com.nextsites.model;

import br.com.nextsites.dto.UsuarioDto;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 13:41
 */


@Entity
@Table(name = "usuario")
public @Data class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 20)
    private String senha;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "nivel_id")
    private Grupo nivel;

    @ManyToMany(mappedBy = "usuarios")
    private List<Arquivo> arquivos;

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(UsuarioDto usuarioDto) {
        this.nome = usuarioDto.getNome();
        this.email = usuarioDto.getEmail();
        this.senha = usuarioDto.getSenha();
        this.nivel = new Grupo(usuarioDto.getNivel().getId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
