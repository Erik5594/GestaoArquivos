package br.com.nextsites.dto;

import lombok.Data;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 29/05/2020 - 15:42
 */
public @Data class PermissaoDto {
    private Long idUsuario;
    private Long idArquivo;

    public PermissaoDto() {
    }

    public PermissaoDto(Long idUsuario, Long idArquivo) {
        this.idUsuario = idUsuario;
        this.idArquivo = idArquivo;
    }
}
