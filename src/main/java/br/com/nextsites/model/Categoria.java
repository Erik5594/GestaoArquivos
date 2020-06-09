package br.com.nextsites.model;

import br.com.nextsites.dto.CategoriaDto;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 08/06/2020 - 19:51
 */

@Entity
@Table(name = "categoria")
public @Data class Categoria {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;

    @Column(nullable = false, length = 100)
    private String nomeCategoria;

    @OneToMany(mappedBy = "categoriaPai", cascade = CascadeType.ALL)
    private List<Categoria> subCategorias;

    public Categoria(CategoriaDto categoriaDto) {
        this.id = categoriaDto.getId();
        this.nomeCategoria = categoriaDto.getNomeCategoria();
        if(categoriaDto.getCategoriaPai() != null){
            Long idCategoriaPai = categoriaDto.getCategoriaPai().getId();
            String nomeCategoriaPai = categoriaDto.getCategoriaPai().getNomeCategoria();
            this.categoriaPai = new Categoria(idCategoriaPai, nomeCategoriaPai);
        }
        if(categoriaDto.getSubCategorias() != null){
            this.subCategorias = new ArrayList<>();
            for(CategoriaDto categoriaDto1 : categoriaDto.getSubCategorias()){
                this.subCategorias.add(new Categoria(categoriaDto1.getId(), categoriaDto1.getNomeCategoria()));
            }
        }
    }

    public Categoria(Long id, String nomeCategoria){
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }

    public Categoria() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Categoria other = (Categoria) obj;
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
