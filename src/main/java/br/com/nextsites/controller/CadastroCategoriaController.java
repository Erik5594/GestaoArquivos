package br.com.nextsites.controller;

import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.service.CategoriaService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 08/06/2020 - 19:50
 */

@Named
@ViewScoped
public class CadastroCategoriaController {

    @Getter @Setter
    private CategoriaDto categoria;

    @Inject
    private CategoriaService categoriaService;

    @Inject
    private FacesUtil facesUtil;

    @Getter @Setter
    private boolean adcCategoriaPai;

    @Getter @Setter
    private TreeNode categoriaPai;

    @Getter @Setter
    private TreeNode selectedNode;

    public void inicializar(){
        if(FacesUtil.isNotPostback()){
            if(categoria == null){
                categoria = new CategoriaDto();
            }
            carregarArvoreCategoria();
        }
    }

    private void carregarArvoreCategoria(){
        categoriaPai = new DefaultTreeNode();

        List<CategoriaDto> categorias = categoriaService.buscarTodas();

        for(CategoriaDto categoriaDto : categorias){
            categoriaPai = categoriaService.retornoArvoreCategoria(categoriaPai, categoriaDto.getId());
        }
    }

    public void salvar(){
        try {
            if(StringUtils.isNotBlank(categoria.getNomeCategoria())){
                categoriaService.salvar(categoria);
                facesUtil.addInfoMessage("Nova categoria criada com sucesso!");
                limpar();
            }else{
                FacesUtil.addErrorMessage("Deve ser informado um nome para a nova categoria!");
            }
        }catch (Exception e){
            facesUtil.addErrorMessage(e.getMessage());
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        categoria.setCategoriaPai((CategoriaDto) event.getTreeNode().getData());
        abrirTreeNode(event.getTreeNode());
    }

    private void abrirTreeNode(TreeNode treeNode){
        if(treeNode != null){
            treeNode.setExpanded(true);
            if(treeNode.getParent() != null){
                abrirTreeNode(treeNode.getParent());
            }
        }
    }

    private void limpar(){
        categoria = new CategoriaDto();
        carregarArvoreCategoria();
        adcCategoriaPai = false;
    }
}
