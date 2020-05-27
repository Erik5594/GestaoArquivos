package br.com.nextsites.dto;

import br.com.nextsites.model.Usuario;
import lombok.Data;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 22/05/2020 - 17:15
 */
public @Data class UsuarioDto {
    private Long id;
    private String nome;
    private Long nivel;
    private String email;
    private String senha;

    public UsuarioDto() {
    }

    public UsuarioDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.nivel = usuario.getNivel().getId();
    }
}
