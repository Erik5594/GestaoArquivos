package br.com.nextsites.dto;

import br.com.nextsites.model.Grupo;
import lombok.Data;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 24/05/2020 - 10:02
 */
public @Data class GrupoDto {
    private Long id;
    private String nome;
    private String descricao;

    public GrupoDto() {
    }

    public GrupoDto(Grupo grupo) {
        this.id = grupo.getId();
        this.nome = grupo.getNome();
        this.descricao = grupo.getDescricao();
    }
}
