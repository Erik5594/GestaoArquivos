package br.com.nextsites.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 26/05/2020 - 21:20
 */
public @Data class ArquivoDto {
    private Long id;
    private String nome;
    private String diretorio;
    private byte[] conteudo;
    private Date dataEnvio;
    private List<UsuarioDto> listUsuarios;
}
