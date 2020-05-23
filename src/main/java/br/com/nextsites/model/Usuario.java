package br.com.nextsites.model;

import lombok.Data;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 23/05/2020 - 13:41
 */

public @Data class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private Permissao permissao;
}
