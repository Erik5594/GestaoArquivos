package br.com.nextsites.dto;

import br.com.nextsites.model.Categoria;
import lombok.Data;

import java.util.ArrayList;
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
    private CategoriaDto categoriaPai;
    private List<CategoriaDto> subCategorias;

    public CategoriaDto(Categoria categoria) {
        this.id = categoria.getId();
        this.nomeCategoria = categoria.getNomeCategoria();
        if(categoria.getCategoriaPai() != null){
            Long idCategoriaPai = categoria.getCategoriaPai().getId();
            String nomeCategoriaPai = categoria.getCategoriaPai().getNomeCategoria();
            this.categoriaPai = new CategoriaDto(idCategoriaPai, nomeCategoriaPai);
        }
        if(categoria.getSubCategorias() != null){
            this.subCategorias = new ArrayList<>();
            for(Categoria categoria1 : categoria.getSubCategorias()){
                subCategorias.add(new CategoriaDto(categoria1.getId(), categoria1.getNomeCategoria()));
            }
        }
    }


    public CategoriaDto(long id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }


    public CategoriaDto() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoriaDto other = (CategoriaDto) obj;
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