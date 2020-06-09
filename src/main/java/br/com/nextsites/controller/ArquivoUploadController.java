package br.com.nextsites.controller;

import br.com.nextsites.dto.*;
import br.com.nextsites.service.ArquivoService;
import br.com.nextsites.service.CategoriaService;
import br.com.nextsites.service.UsuarioService;
import br.com.nextsites.util.file.FileUtil;
import br.com.nextsites.util.jsf.FacesUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
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
    private List<CategoriaOldDto> categorias;

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
    private boolean continuar = false;
    @Getter @Setter
    private List<ArquivoDto> arquivos;

    @Getter @Setter
    private TreeNode root;

    @Getter @Setter
    private TreeNode selectedNode;

    @Inject
    private CategoriaService categoriaService;

    @PostConstruct
    public void init(){
        root = new DefaultTreeNode();
        Long lon = 198l;
        for(int i=0; i<2; i++){
            lon = lon+2;
            root = categoriaService.retornoArvoreCategoria(root, lon);
        }
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
        CategoriaOldDto novaCategoria = new CategoriaOldDto();
        novaCategoria.setEditavel(true);
        categorias.add(novaCategoria);
    }

    public void removerCategoria(){
        if(categorias.size() > 1){
            categorias.remove(categorias.size()-1);
            CategoriaOldDto categoria = categorias.get(categorias.size()-1);
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
            FacesUtil.addWarnMessage("A opção 'Gerenciar Permissões' só deve ser clicada quando for finalizado o carregamento de todos arquivos.");
        }
    }

    private String getCaminhoAtual(){
        String caminho = "";
        for(CategoriaOldDto categoria : categorias){
            if(StringUtils.isNotBlank(categoria.getNomeCategoria())){
                caminho = caminho + categoria.getNomeCategoria().toUpperCase()+FileUtil.SEPARADOR;
            }
        }
        return caminho;
    }

    public void salvar(){
        if(validar()){
            List<UsuarioDto> usuarioComPermissao = usuarios.getTarget();
            List<PermissaoDto> adcionarPermissoes = new ArrayList<>();

            for(ArquivoDto arquivoDto : arquivos){
                for(UsuarioDto usuario : usuarioComPermissao){
                    adcionarPermissoes.add(new PermissaoDto(usuario.getId(), arquivoDto.getId()));
                }
            }

            arquivoService.incluirPermissoes(adcionarPermissoes);

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
        if((usuarios.getTarget() == null || usuarios.getTarget().isEmpty()) && !continuar){
            retorno = false;
            continuar = true;
            FacesUtil.addErrorMessage("Nenhum cliente foi selecionado para ter acesso aos arquivos carregados. Caso deseje continuar basta clicar em 'Salvar' novamente!");
        }
        return retorno;
    }

    private void limpar(){
        categorias = new ArrayList<>();
        adcionarCategoria();
        inicializarPickList();
        caminhoConcluido = "";
        arquivos = new ArrayList<>();
        exibirCategoria = true;
        exibirUploadFiles = false;
        exibirPermissao = false;
        continuar = false;
    }

    public void carregarArquivo(FileUploadEvent event) {
        try {
            arquivoService.gravarArquivo(caminhoConcluido, event.getFile());
            addArquivo(event.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gerenciarPermissoes(){
        if(arquivos != null && !arquivos.isEmpty()){
            exibirUploadFiles = false;
            exibirCategoria = false;
            exibirPermissao = true;
        } else {
            FacesUtil.addErrorMessage("Deve ser carregado pelo menos 1 arquivo.");
        }
    }

    private synchronized void addArquivo(UploadedFile file){
        if(arquivos == null){
            arquivos = new ArrayList<>();
        }
        ArquivoDto arquivoDto = new ArquivoDto();
        arquivoDto.setNome(file.getFileName());
        arquivoDto.setDiretorio(caminhoConcluido);
        arquivoDto.setConteudo(file.getContent());
        arquivoDto.setDataEnvio(new Date());
        arquivoDto = arquivoService.salvarDocumento(arquivoDto);
        arquivos.add(arquivoDto);
    }



    public void onNodeExpand(NodeExpandEvent event) {
        FacesUtil.addInfoMessage("Expanded "+ event.getTreeNode().toString());
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesUtil.addInfoMessage("Collapsed "+ event.getTreeNode().toString());
    }

    public void onNodeSelect(NodeSelectEvent event) {
        FacesUtil.addInfoMessage("Selected "+ event.getTreeNode().toString());
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesUtil.addInfoMessage("Unselected "+ event.getTreeNode().toString());
    }
}
