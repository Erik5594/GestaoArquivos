package br.com.nextsites.controller;

import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Next Solucoes
 *
 * @author erik_
 * Data Criacao: 25/05/2020 - 14:10
 */

@Named
@ViewScoped
public class ArquivoUploadController {

    @Getter @Setter
    private CategoriaDto categoriaDto;

    @Getter @Setter
    private List<CategoriaDto> categorias;

    @Getter @Setter
    private List<UsuarioDto> usuarios;

    @Getter @Setter
    private List<UsuarioDto> usuariosSelecionados;

    @Getter @Setter
    private String concluido;

    @Inject
    private UsuarioService usuarioService;

    @PostConstruct
    public void init(){
        usuarios = new ArrayList<>();
        usuarios = usuarioService.getListaClientes();
    }

    public void inicializar() {
        if (FacesUtil.isNotPostback()) {
            categorias = new ArrayList<>();
            adcionarNovaCategoria();
            if(categoriaDto == null){
                limpar();
            }
        }
    }

    public void adcionarNovaCategoria(){
        System.out.println("Adcionando Categoria");
        if(categorias.size() > 0){
            categorias.get(categorias.size()-1).setEditavel(false);
        }
        CategoriaDto novaCategoria = new CategoriaDto();
        novaCategoria.setEditavel(true);
        categorias.add(novaCategoria);
    }

    public void removerCategoria(){
        System.out.println("Remover Categoria");
        if(categorias.size() > 1){
            categorias.remove(categorias.size()-1);
            categorias.get(categorias.size()-1).setEditavel(true);
        }
    }

    public void concluir(){
        concluido = "";
        for(CategoriaDto categoria : categorias){
            concluido = concluido + "/"+categoria.getNomeCategoria().toUpperCase();
        }

    }

    public void salvar(){
        System.out.println("clicou em salvar");
        FacesUtil.addInfoMessage("Clicou em salvar");
    }

    private void limpar(){
        categoriaDto = new CategoriaDto();
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
