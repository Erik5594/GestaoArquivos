package br.com.nextsites.dto;

import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;
import lombok.Data;

import java.util.ArrayList;
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

    public ArquivoDto() {
    }

    public ArquivoDto(Arquivo arquivo) {
        this.id = arquivo.getId();
        this.nome = arquivo.getNome();
        this.diretorio = arquivo.getDiretorio();
        this.conteudo = arquivo.getConteudo();
        this.dataEnvio = arquivo.getDataEnvio();
        if(arquivo.getUsuarios() != null){
            this.listUsuarios = new ArrayList<>();
            for(Usuario usuario : arquivo.getUsuarios()) {
                listUsuarios.add(new UsuarioDto(usuario));
            }
        }
    }
}
