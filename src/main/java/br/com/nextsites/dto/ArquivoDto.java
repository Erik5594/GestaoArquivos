package br.com.nextsites.dto;

import br.com.nextsites.model.Arquivo;
import br.com.nextsites.model.Usuario;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
    private byte[] conteudo;
    private Date dataEnvio;
    private List<UsuarioDto> listUsuarios;
    private CategoriaDto categoriaDto;

    public ArquivoDto() {
    }

    public ArquivoDto(Arquivo arquivo) {
        this.id = arquivo.getId();
        this.nome = arquivo.getNome();
        this.conteudo = arquivo.getConteudo();
        this.dataEnvio = arquivo.getDataEnvio();
        if(arquivo.getUsuarios() != null){
            this.listUsuarios = new ArrayList<>();
            for(Usuario usuario : arquivo.getUsuarios()) {
                listUsuarios.add(new UsuarioDto(usuario));
            }
        }
        this.categoriaDto = new CategoriaDto(arquivo.getCategoria());
    }

    public String getNome(){
        if(StringUtils.isNotBlank(this.nome)){
            if(this.nome.length() > 4){
                if(!".pdf".equalsIgnoreCase(this.nome.substring(this.nome.length() -4, this.nome.length()))){
                    return this.nome + ".pdf";
                }
            }else{
                return this.nome + ".pdf";
            }
        }
        return this.nome;
    }

    public StreamedContent getFile() {
        StreamedContent file = DefaultStreamedContent.builder()
                .name(this.nome)
                .contentType("application/pdf")
                .stream(() -> new ByteArrayInputStream(this.conteudo))
                .build();

        return file;
    }

    public String getNomeAbreviado(){
        if(StringUtils.isNotBlank(this.nome) && this.nome.length() > 10){
            return this.nome.substring(0,9)+"...";
        }
        return this.nome;
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
