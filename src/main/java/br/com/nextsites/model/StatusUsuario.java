package br.com.nextsites.model;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 28/05/2020 - 22:13
 */
public enum StatusUsuario {

    ATIVO("Ativo"),
    INATIVO("Inativo");

    private String descricao;

    StatusUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
