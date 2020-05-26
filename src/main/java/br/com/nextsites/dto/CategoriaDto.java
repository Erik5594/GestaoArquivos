package br.com.nextsites.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 25/05/2020 - 14:11
 */
public @Data class CategoriaDto {
    private Long id;
    private String nomeCategoria;
    private List<Long> idSubCategoria;
    private boolean editavel;
}
