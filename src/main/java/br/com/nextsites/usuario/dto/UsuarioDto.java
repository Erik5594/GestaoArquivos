package br.com.nextsites.usuario.dto;

import lombok.Data;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 22/05/2020 - 17:15
 */
public @Data class UsuarioDto {
    private String nome;
    private String nivel;
    private String email;
    private String senha;
}
