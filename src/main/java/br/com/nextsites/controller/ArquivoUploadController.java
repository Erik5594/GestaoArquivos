package br.com.nextsites.controller;

import br.com.nextsites.dto.CategoriaDto;
import br.com.nextsites.dto.UsuarioDto;
import br.com.nextsites.service.ArquivoService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.file.FileUtil;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
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
    private String caminhoConcluido;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private ArquivoService arquivoService;

    @Getter @Setter
    private boolean exibirCategoria = true;
    @Getter @Setter
    private boolean exibirUploadFiles = true;
    @Getter @Setter
    private boolean exibirPermissao = true;
    @Getter @Setter
    private List<UploadedFile> files;

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

    public List<String> completeNome(String start) {
        return arquivoService.nomePastas(getCaminhoAtual(), start.toUpperCase());
    }

    public void adcionarNovaCategoria(){
        System.out.println("Adcionando Categoria");
        if(categorias.size() > 0){
            categorias.get(categorias.size()-1).setEditavel(false);
        }
        System.out.println(System.getProperty("user.home"));
        CategoriaDto novaCategoria = new CategoriaDto();
        novaCategoria.setEditavel(true);
        categorias.add(novaCategoria);
    }

    public void removerCategoria(){
        if(categorias.size() > 1){
            categorias.remove(categorias.size()-1);
            CategoriaDto categoria = categorias.get(categorias.size()-1);
            categoria.setEditavel(true);
            categoria.setNomeCategoria("");
        }
    }

    public void concluirCaminho(){
        caminhoConcluido = getCaminhoAtual();
        if(StringUtils.isNotBlank(caminhoConcluido) && !FileUtil.SEPARADOR.equals(caminhoConcluido)) {
            arquivoService.nomePastas(caminhoConcluido, null);
            exibirCategoria = false;
            exibirUploadFiles = true;
            exibirPermissao = false;
        }
    }

    private String getCaminhoAtual(){
        String caminho = "";
        for(CategoriaDto categoria : categorias){
            if(StringUtils.isNotBlank(categoria.getNomeCategoria())){
                caminho = caminho + categoria.getNomeCategoria().toUpperCase()+FileUtil.SEPARADOR;
            }
        }
        return caminho;
    }

    public void salvar(){
        System.out.println("clicou em salvar");
        FacesUtil.addInfoMessage("Clicou em salvar");
        if(files != null && !files.isEmpty()){
            for(UploadedFile file : files){
                try{
                    arquivoService.gravarArquivo(caminhoConcluido, file);
                }catch (IOException e){
                    //ignorar
                }
            }
        }
    }

    private void limpar(){
        categoriaDto = new CategoriaDto();
    }

    public void handleFileUpload(FileUploadEvent event) {
        System.out.println(event.getFile().getFileName());
        addFile(event.getFile());
        try {
            arquivoService.gravarArquivo(caminhoConcluido, event.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        exibirUploadFiles = false;
        exibirCategoria = false;
        exibirPermissao = true;
    }

    public void addFile(UploadedFile file) {
        if(this.files == null){
            this.files = new ArrayList<>();
        }
        this.files.add(file);
    }
}
