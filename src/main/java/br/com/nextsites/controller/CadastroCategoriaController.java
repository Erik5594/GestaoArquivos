package br.com.nextsites.controller;

import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.service.CategoriaService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;

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
    private List<CategoriaDto> categorias;

    public void inicializar(){
        if(categoria == null){
            categoria = new CategoriaDto();
        }
        categorias = categoriaService.buscarTodas();
    }

    public void salvar(){
        try {
            categoriaService.salvar(categoria);
            facesUtil.addInfoMessage("Sucesso!");
        }catch (Exception e){
            facesUtil.addErrorMessage(e.getMessage());
        }
    }
}
