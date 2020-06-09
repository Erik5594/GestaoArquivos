package br.com.nextsites.service;

import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.dto.Document;
import br.com.nextsites.model.Categoria;
import br.com.nextsites.repository.Categorias;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 08/06/2020 - 19:51
 */
public class CategoriaService {
    @Inject
    private Categorias categoriaDao;

    public List<CategoriaDto> buscarTodas() {
        return converterListArquivo(categoriaDao.buscarTodas());
    }

    private List<CategoriaDto> converterListArquivo(List<Categoria> categorias){
        List<CategoriaDto> categoriasDto = null;
        if(categorias!= null){
            categoriasDto = new ArrayList<>();
            for(Categoria categoria : categorias){
                categoriasDto.add(new CategoriaDto(categoria));
            }
        }
        return categoriasDto;
    }

    public CategoriaDto salvar(CategoriaDto categoriaDto){
        Categoria categoria = categoriaDao.salvar(new Categoria(categoriaDto));
        return new CategoriaDto(categoria);
    }

    public CategoriaDto findById(Long idCategoria){
        Categoria categoria = categoriaDao.findById(idCategoria);
        if(categoria != null){
            return new CategoriaDto(categoria);
        }
        return null;
    }

    public TreeNode retornoArvoreCategoria(TreeNode root, Long idCategoria){
        TreeNode arvore =  criarCategoriaPai(idCategoria, null, root);

        root.getChildren().add(arvore);

        return root;
    }

    private TreeNode criarCategoriaPai(Long idNovaCategoriaPaiDto, TreeNode categoriaFilha, TreeNode root){
        CategoriaDto novaCategoriaPaiDto = new CategoriaDto(categoriaDao.findById(idNovaCategoriaPaiDto));

        TreeNode categoriaPai = getTree(novaCategoriaPaiDto, root.getChildren());

        if(categoriaFilha != null){
            categoriaPai.getChildren().add(categoriaFilha);
        }
        if(novaCategoriaPaiDto.getCategoriaPai() != null){
            return criarCategoriaPai(novaCategoriaPaiDto.getCategoriaPai().getId(), categoriaPai, root);
        }else{
            return categoriaPai;
        }
    }

    private TreeNode getTree(CategoriaDto novaCategoriaPaiDto, List<TreeNode> categorias){
        TreeNode treeNodeRetorno = getTreeExistente(novaCategoriaPaiDto, categorias);
        if(treeNodeRetorno == null){
            treeNodeRetorno = new DefaultTreeNode(novaCategoriaPaiDto, null);
        }
        return treeNodeRetorno;
    }

    private TreeNode getTreeExistente(CategoriaDto novaCategoriaPaiDto, List<TreeNode> categorias){
        if(categorias != null && !categorias.isEmpty()){
            for(TreeNode pasta : categorias){
                if(pasta.getData() != null && pasta.getData().equals(novaCategoriaPaiDto)){
                    return pasta;
                }else if (pasta.getChildren() != null && !pasta.getChildren().isEmpty()){
                    TreeNode treeNode = getTree(novaCategoriaPaiDto, pasta.getChildren());
                    if(treeNode != null){
                        return treeNode;
                    }
                }
            }
        }
        return null;
    }
}
