package br.com.nextsites.controller;

import br.com.nextsites.dto.ArquivoDto;
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
import org.primefaces.model.DualListModel;
import org.primefaces.model.file.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private DualListModel<UsuarioDto> usuarios;

    @Getter @Setter
    private String caminhoConcluido;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private ArquivoService arquivoService;

    @Getter @Setter
    private boolean exibirCategoria = true;
    @Getter @Setter
    private boolean exibirUploadFiles = false;
    @Getter @Setter
    private boolean exibirPermissao = false;
    @Getter @Setter
    private List<ArquivoDto> arquivos;

    @PostConstruct
    public void init(){
        inicializarPickList();
    }

    private void inicializarPickList(){
        List<UsuarioDto> listUsuariosSource = usuarioService.getListaClientes();
        List<UsuarioDto> listUsuariosTarget = new ArrayList<>();
        usuarios = new DualListModel<>(listUsuariosSource, listUsuariosTarget);
    }

    public void inicializar() {
        if (FacesUtil.isNotPostback()) {
            categorias = new ArrayList<>();
            adcionarCategoria();
            if(categoriaDto == null){
                limpar();
            }
        }
    }

    public List<String> completeNome(String start) {
        return arquivoService.nomePastas(getCaminhoAtual(), start.toUpperCase());
    }

    public void adcionarCategoria(){
        if(categorias.size() > 0){
            if(StringUtils.isNotBlank(categorias.get(categorias.size()-1).getNomeCategoria())){
                categorias.get(categorias.size()-1).setEditavel(false);
                adcionarNovaCategoria();
            }
        }else {
            adcionarNovaCategoria();
        }
    }

    private void adcionarNovaCategoria() {
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
        if(validar()){
            List<Long> listIdUsuarios = new ArrayList<>();
            for(UsuarioDto usuario : usuarios.getTarget()){
                listIdUsuarios.add(usuario.getId());
            }
            for(ArquivoDto arquivoDto : arquivos){
                arquivoDto.setListIdUsuarios(listIdUsuarios);
            }
            for(ArquivoDto arquivoDto : arquivos){
                arquivoDto.setDataEnvio(new Date());
                arquivoService.salvarDocumento(arquivoDto);
            }
            limpar();
            FacesUtil.addInfoMessage("Documento salvo com sucesso!");
        }
    }

    private boolean validar(){
        boolean retorno = true;
        if(arquivos == null || arquivos.isEmpty()){
            retorno = false;
            FacesUtil.addErrorMessage("Nenhum arquivo carregado!");
        }
        if(usuarios.getTarget() == null || usuarios.getTarget().isEmpty()){
            retorno = false;
            FacesUtil.addErrorMessage("Nenhum cliente selecionado!");
        }
        return retorno;
    }

    private void limpar(){
        categoriaDto = new CategoriaDto();
        categorias = new ArrayList<>();
        adcionarCategoria();
        inicializarPickList();
        caminhoConcluido = "";
        arquivos = new ArrayList<>();
        exibirCategoria = true;
        exibirUploadFiles = false;
        exibirPermissao = false;
    }

    public void carregarArquivo(FileUploadEvent event) {
        try {
            arquivoService.gravarArquivo(caminhoConcluido, event.getFile());
            addArquivo(event.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        exibirUploadFiles = false;
        exibirCategoria = false;
        exibirPermissao = true;
    }

    private void addArquivo(UploadedFile file){
        if(arquivos == null){
            arquivos = new ArrayList<>();
        }
        ArquivoDto arquivoDto = new ArquivoDto();
        arquivoDto.setNome(file.getFileName());
        arquivoDto.setDiretorio(caminhoConcluido);
        arquivoDto.setConteudo(file.getContent());
        arquivos.add(arquivoDto);
    }
}
