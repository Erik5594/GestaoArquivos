package br.com.nextsites.dto;

import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;
import lombok.Data;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
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

    public StreamedContent getFile() {
        StreamedContent file = DefaultStreamedContent.builder()
                .name(this.nome)
                .contentType("application/pdf")
                .stream(() -> new ByteArrayInputStream(this.conteudo))
                .build();

        return file;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArquivoDto other = (ArquivoDto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
